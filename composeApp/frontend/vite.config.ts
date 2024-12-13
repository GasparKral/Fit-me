import {defineConfig} from 'vite'
import react from '@vitejs/plugin-react-swc'
import path from "path"

// https://vite.dev/config/
export default defineConfig({
  resolve: {
    alias: {
      '@kotlin': path.resolve(__dirname, '../build/js/packages/composeApp/kotlin'),
    },
  },
  server: {
    port: 3000,
  },
  build: {
    outDir: "dist"
  },
  plugins: [react()],
})
