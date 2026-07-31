const { defineConfig, devices } = require('@playwright/test');
module.exports = defineConfig({
  testDir: './',
  fullyParallel: false,
  forbidOnly: !!process.env.CI,
  retries: process.env.CI ? 2 : 0,
  reporter: [['html', { outputFolder: '../../reports/e2e/html', open: 'never' }], ['json', { outputFile: '../../reports/e2e/results.json' }]],
  use: { baseURL: process.env.BASE_URL || 'http://localhost:8080', trace: 'on-first-retry' },
  projects: [{ name: 'chromium', use: { ...devices['Desktop Chrome'] } }],
});
