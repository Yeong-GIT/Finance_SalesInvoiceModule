import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  build: {
    rollupOptions: {
      external: [
        'datatables.net',
        'datatables.net-responsive',
        'datatables.net-dt/css/jquery.dataTables.min.css',
        'datatables.net-responsive-dt/css/responsive.dataTables.min.css'
      ]
    }
  }
})
