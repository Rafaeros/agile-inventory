/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/main/webapp/WEB-INF/jsp/**/*.jsp",
    "./src/main/resources/static/js/**/*.js" // Adicione se você usar JS
  ],
  theme: {
    extend: {},
  },
  plugins: [],
}