package es.gaspardev.enums.grip

enum class Grip(val type: Type, val specificGrip: String) {
    // Pulley Grips
    OVERHAND(Type.PULLEY, PulleyGrip.OVERHAND.name),
    UNDERHAND(Type.PULLEY, PulleyGrip.UNDERHAND.name),
    NEUTRAL(Type.PULLEY, PulleyGrip.NEUTRAL.name),
    WIDE(Type.PULLEY, PulleyGrip.WIDE.name),
    CLOSE(Type.PULLEY, PulleyGrip.CLOSE.name),
    REVERSIBLE(Type.PULLEY, PulleyGrip.REVERSIBLE.name),
    HAMMER(Type.PULLEY, PulleyGrip.HAMMER.name),
    SUPINATED(Type.PULLEY, PulleyGrip.SUPINATED.name),
    PRONATED(Type.PULLEY, PulleyGrip.PRONATED.name),
    MIXED(Type.PULLEY, PulleyGrip.MIXED.name),
    STRAIGHT_BAR_GRIP(Type.PULLEY, PulleyGrip.STRAIGHT_BAR.name),
    ROPE(Type.PULLEY, PulleyGrip.ROPE.name),
    V_BAR(Type.PULLEY, PulleyGrip.V_BAR.name),
    EZ_BAR(Type.PULLEY, PulleyGrip.EZ_BAR.name),
    D_HANDLE(Type.PULLEY, PulleyGrip.D_HANDLE.name),

    // Bars
    STRAIGHT(Type.BAR, Bars.STRAIGHT.name),
    OLYMPIC(Type.BAR, Bars.OLYMPIC.name),
    EZ_CURL(Type.BAR, Bars.EZ_CURL.name),
    TRAP(Type.BAR, Bars.TRAP.name),
    SAFETY_SQUAT(Type.BAR, Bars.SAFETY_SQUAT.name),
    CAMBERED(Type.BAR, Bars.CAMBERED.name),
    SWISS(Type.BAR, Bars.SWISS.name),
    AXLE(Type.BAR, Bars.AXLE.name),
    BUFFALO(Type.BAR, Bars.BUFFALO.name),
    MULTI_GRIP(Type.BAR, Bars.MULTI_GRIP.name),
    TRICEPS(Type.BAR, Bars.TRICEPS.name),
    POWER(Type.BAR, Bars.POWER.name),
    DEADLIFT(Type.BAR, Bars.DEADLIFT.name),
    TECHNIQUE(Type.BAR, Bars.TECHNIQUE.name),
    LOG(Type.BAR, Bars.LOG.name),
    WOMENS_OLYMPIC(Type.BAR, Bars.WOMENS_OLYMPIC.name),
    YOKE(Type.BAR, Bars.YOKE.name),
    FARMERS_WALK_HANDLES(Type.BAR, Bars.FARMERS_WALK_HANDLES.name),
    SQUAT(Type.BAR, Bars.SQUAT.name),
    PREGNANCY_SAFETY(Type.BAR, Bars.PREGNANCY_SAFETY.name);

    enum class Type {
        PULLEY,
        BAR
    }
}

private enum class PulleyGrip {
    OVERHAND,
    UNDERHAND,
    NEUTRAL,
    WIDE,
    CLOSE,
    REVERSIBLE,
    HAMMER,
    SUPINATED,
    PRONATED,
    MIXED,
    STRAIGHT_BAR,
    ROPE,
    V_BAR,
    EZ_BAR,
    D_HANDLE
}

private enum class Bars {
    STRAIGHT,
    OLYMPIC,
    EZ_CURL,
    TRAP,
    SAFETY_SQUAT,
    CAMBERED,
    SWISS,
    AXLE,
    BUFFALO,
    MULTI_GRIP,
    TRICEPS,
    POWER,
    DEADLIFT,
    TECHNIQUE,
    LOG,
    WOMENS_OLYMPIC,
    YOKE,
    FARMERS_WALK_HANDLES,
    SQUAT,
    PREGNANCY_SAFETY
}
