import { defineConfig } from 'vite'
import react from '@sites/plugin-react'

export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
  },
})
