-- ============================================================================
-- 05-settings-tables.sql
-- Tablas de configuraciones de usuarios
-- ============================================================================

-- ============================================================================
-- ENUMS PARA CONFIGURACIONES
-- ============================================================================

-- Theme Enum
CREATE TYPE theme_enum AS ENUM (
    'LIGHT',
    'DARK', 
    'SYSTEM'
);

-- Font Size Enum
CREATE TYPE font_size_enum AS ENUM (
    'SMALL',
    'MEDIUM',
    'LARGE'
);

-- Language Enum
CREATE TYPE language_enum AS ENUM (
    'ES',
    'EN',
    'FR',
    'DE',
    'IT',
    'PT'
);

-- ============================================================================
-- TABLAS DE CONFIGURACIONES
-- ============================================================================

-- Tabla de configuraciones de cuenta y seguridad
CREATE TABLE account_settings (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    email_notifications BOOLEAN DEFAULT true,
    sms_notifications BOOLEAN DEFAULT false,
    two_factor_enabled BOOLEAN DEFAULT false,
    session_timeout INTEGER DEFAULT 60, -- en minutos
    auto_logout BOOLEAN DEFAULT false,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    -- Índice único para un registro por usuario
    UNIQUE(user_id)
);

-- Tabla de configuraciones de apariencia
CREATE TABLE appearance_settings (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    theme theme_enum DEFAULT 'SYSTEM',
    accent_color VARCHAR(7) DEFAULT '#2196F3', -- color hex
    font_size font_size_enum DEFAULT 'MEDIUM',
    compact_mode BOOLEAN DEFAULT false,
    show_sidebar BOOLEAN DEFAULT true,
    animations_enabled BOOLEAN DEFAULT true,
    language language_enum DEFAULT 'ES',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    -- Índice único para un registro por usuario
    UNIQUE(user_id)
);

-- Tabla de configuraciones de notificaciones
CREATE TABLE notification_settings (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    push_notifications BOOLEAN DEFAULT true,
    email_notifications BOOLEAN DEFAULT true,
    sms_notifications BOOLEAN DEFAULT false,
    workout_reminders BOOLEAN DEFAULT true,
    session_reminders BOOLEAN DEFAULT true,
    message_notifications BOOLEAN DEFAULT true,
    progress_reports BOOLEAN DEFAULT true,
    marketing_emails BOOLEAN DEFAULT false,
    sound BOOLEAN DEFAULT true,
    vibration BOOLEAN DEFAULT true,
    notification_time TIME DEFAULT '09:00:00', -- formato HH:mm:ss
    quiet_hours_start TIME DEFAULT '22:00:00', -- formato HH:mm:ss
    quiet_hours_end TIME DEFAULT '07:00:00', -- formato HH:mm:ss
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    -- Índice único para un registro por usuario
    UNIQUE(user_id)
);

-- ============================================================================
-- ÍNDICES PARA RENDIMIENTO
-- ============================================================================

-- Índices para configuraciones de cuenta
CREATE INDEX idx_account_settings_user_id ON account_settings(user_id);
CREATE INDEX idx_account_settings_updated_at ON account_settings(updated_at);

-- Índices para configuraciones de apariencia
CREATE INDEX idx_appearance_settings_user_id ON appearance_settings(user_id);
CREATE INDEX idx_appearance_settings_theme ON appearance_settings(theme);
CREATE INDEX idx_appearance_settings_language ON appearance_settings(language);

-- Índices para configuraciones de notificaciones
CREATE INDEX idx_notification_settings_user_id ON notification_settings(user_id);
CREATE INDEX idx_notification_settings_push ON notification_settings(push_notifications);
CREATE INDEX idx_notification_settings_email ON notification_settings(email_notifications);

-- ============================================================================
-- TRIGGERS PARA UPDATED_AT AUTOMÁTICO
-- ============================================================================

-- Función para actualizar timestamp automáticamente
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Triggers para actualizar updated_at automáticamente
CREATE TRIGGER update_account_settings_updated_at 
    BEFORE UPDATE ON account_settings 
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_appearance_settings_updated_at 
    BEFORE UPDATE ON appearance_settings 
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_notification_settings_updated_at 
    BEFORE UPDATE ON notification_settings 
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- ============================================================================
-- DATOS INICIALES POR DEFECTO
-- ============================================================================

-- Función para crear configuraciones por defecto para un nuevo usuario
CREATE OR REPLACE FUNCTION create_default_user_settings(user_id_param INTEGER)
RETURNS VOID AS $$
BEGIN
    -- Insertar configuraciones de cuenta por defecto
    INSERT INTO account_settings (user_id)
    VALUES (user_id_param)
    ON CONFLICT (user_id) DO NOTHING;
    
    -- Insertar configuraciones de apariencia por defecto
    INSERT INTO appearance_settings (user_id)
    VALUES (user_id_param)
    ON CONFLICT (user_id) DO NOTHING;
    
    -- Insertar configuraciones de notificaciones por defecto
    INSERT INTO notification_settings (user_id)
    VALUES (user_id_param)
    ON CONFLICT (user_id) DO NOTHING;
END;
$$ LANGUAGE plpgsql;

-- ============================================================================
-- TRIGGER PARA CREAR CONFIGURACIONES AUTOMÁTICAMENTE
-- ============================================================================

-- Función que se ejecuta cuando se crea un nuevo usuario
CREATE OR REPLACE FUNCTION on_user_created()
RETURNS TRIGGER AS $$
BEGIN
    -- Crear configuraciones por defecto para el nuevo usuario
    PERFORM create_default_user_settings(NEW.id);
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger que se ejecuta después de insertar un nuevo usuario
CREATE TRIGGER trigger_create_user_settings
    AFTER INSERT ON users
    FOR EACH ROW
    EXECUTE FUNCTION on_user_created();

-- ============================================================================
-- VISTAS PARA CONSULTAS OPTIMIZADAS
-- ============================================================================

-- Vista combinada de todas las configuraciones de un usuario
CREATE VIEW user_settings_complete AS
SELECT 
    u.id as user_id,
    u.fullname,
    u.email,
    
    -- Configuraciones de cuenta
    acc.email_notifications as account_email_notifications,
    acc.sms_notifications as account_sms_notifications,
    acc.two_factor_enabled,
    acc.session_timeout,
    acc.auto_logout,
    
    -- Configuraciones de apariencia
    app.theme,
    app.accent_color,
    app.font_size,
    app.compact_mode,
    app.show_sidebar,
    app.animations_enabled,
    app.language,
    
    -- Configuraciones de notificaciones
    notif.push_notifications,
    notif.email_notifications as notification_email_notifications,
    notif.sms_notifications as notification_sms_notifications,
    notif.workout_reminders,
    notif.session_reminders,
    notif.message_notifications,
    notif.progress_reports,
    notif.marketing_emails,
    notif.sound,
    notif.vibration,
    notif.notification_time,
    notif.quiet_hours_start,
    notif.quiet_hours_end
    
FROM users u
LEFT JOIN account_settings acc ON u.id = acc.user_id
LEFT JOIN appearance_settings app ON u.id = app.user_id
LEFT JOIN notification_settings notif ON u.id = notif.user_id;

-- ============================================================================
-- COMENTARIOS EN LAS TABLAS
-- ============================================================================

COMMENT ON TABLE account_settings IS 'Configuraciones de seguridad y cuenta de usuarios';
COMMENT ON TABLE appearance_settings IS 'Configuraciones de apariencia y tema de la interfaz';
COMMENT ON TABLE notification_settings IS 'Configuraciones de notificaciones y alertas';

COMMENT ON COLUMN account_settings.session_timeout IS 'Tiempo de expiración de sesión en minutos';
COMMENT ON COLUMN account_settings.two_factor_enabled IS 'Indica si la autenticación de dos factores está habilitada';

COMMENT ON COLUMN appearance_settings.accent_color IS 'Color de acento en formato hexadecimal (#RRGGBB)';
COMMENT ON COLUMN appearance_settings.compact_mode IS 'Modo compacto de la interfaz';

COMMENT ON COLUMN notification_settings.notification_time IS 'Hora preferida para recibir notificaciones diarias';
COMMENT ON COLUMN notification_settings.quiet_hours_start IS 'Hora de inicio del modo silencioso';
COMMENT ON COLUMN notification_settings.quiet_hours_end IS 'Hora de fin del modo silencioso';

-- ============================================================================
-- FUNCIONES DE UTILIDAD
-- ============================================================================

-- Función para obtener configuraciones completas de un usuario
CREATE OR REPLACE FUNCTION get_user_settings(user_id_param INTEGER)
RETURNS TABLE(
    account_settings JSONB,
    appearance_settings JSONB,
    notification_settings JSONB
) AS $$
BEGIN
    RETURN QUERY
    SELECT 
        row_to_json(acc)::JSONB as account_settings,
        row_to_json(app)::JSONB as appearance_settings,
        row_to_json(notif)::JSONB as notification_settings
    FROM users u
    LEFT JOIN account_settings acc ON u.id = acc.user_id
    LEFT JOIN appearance_settings app ON u.id = app.user_id
    LEFT JOIN notification_settings notif ON u.id = notif.user_id
    WHERE u.id = user_id_param;
END;
$$ LANGUAGE plpgsql;

-- Función para verificar si las horas silenciosas están activas
CREATE OR REPLACE FUNCTION is_quiet_hours_active(user_id_param INTEGER)
RETURNS BOOLEAN AS $$
DECLARE
    current_time TIME := CURRENT_TIME;
    quiet_start TIME;
    quiet_end TIME;
BEGIN
    SELECT quiet_hours_start, quiet_hours_end 
    INTO quiet_start, quiet_end
    FROM notification_settings 
    WHERE user_id = user_id_param;
    
    IF quiet_start IS NULL OR quiet_end IS NULL THEN
        RETURN FALSE;
    END IF;
    
    -- Si quiet_start > quiet_end, significa que cruza medianoche
    IF quiet_start > quiet_end THEN
        RETURN current_time >= quiet_start OR current_time <= quiet_end;
    ELSE
        RETURN current_time >= quiet_start AND current_time <= quiet_end;
    END IF;
END;
$$ LANGUAGE plpgsql;

-- ============================================================================
-- MENSAJE DE CONFIRMACIÓN
-- ============================================================================

DO $$
BEGIN
    RAISE NOTICE 'Tablas de configuraciones de usuarios creadas exitosamente:';
    RAISE NOTICE '- account_settings: Configuraciones de cuenta y seguridad';
    RAISE NOTICE '- appearance_settings: Configuraciones de apariencia e interfaz';
    RAISE NOTICE '- notification_settings: Configuraciones de notificaciones';
    RAISE NOTICE '- Vista user_settings_complete creada para consultas optimizadas';
    RAISE NOTICE '- Triggers automáticos configurados para nuevos usuarios';
    RAISE NOTICE '- Funciones de utilidad disponibles: get_user_settings(), is_quiet_hours_active()';
END $$;
