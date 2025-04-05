import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';
import tailwindcss from 'tailwindcss'; // Import Tailwind CSS
import autoprefixer from 'autoprefixer'; // Import Autoprefixer

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    host: '0.0.0.0', // Allow access from outside the container
    port: 3000,
  },
  build: {
    outDir: 'dist', // Output directory for the build
  },
  css: {
    postcss: {
      plugins: [
        tailwindcss,
        autoprefixer,
      ],
    },
  },
});
