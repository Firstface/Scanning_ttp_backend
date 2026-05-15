import http from 'k6/http';
import { check, sleep } from 'k6';
import { htmlReport } from "https://raw.githubusercontent.com/benc-uk/k6-reporter/main/dist/bundle.js";

export const options = {
  vus: 50,
  duration: '60s',
};

const BASE_URL = 'http://localhost:8080';

export default function () {
  // Health check (lightweight)
  let response = http.get(`${BASE_URL}/actuator/health`);
  check(response, {
    'health is UP': (r) => r.status === 200,
    'health response time < 200ms': (r) => r.timings.duration < 200,
  });
  sleep(0.1);

  // Create task (main workload)
  if (__VU % 3 === 0) {
    const payload = JSON.stringify({
      databaseName: 'demo_db',
      tableName: 'demo_table',
      targetSampleRows: 1000,
      selectedPartitions: ['2026-05-01', '2026-05-02']
    });
    const params = { headers: { 'Content-Type': 'application/json' } };
    response = http.post(`${BASE_URL}/api/sample-tasks`, payload, params);
    check(response, {
      'create task status is 201': (r) => r.status === 201,
      'create task response time < 2000ms': (r) => r.timings.duration < 2000,
    });
    sleep(0.5);
  }
}

export function handleSummary(data) {
  return {
    "reports/k6/summary.html": htmlReport(data),
  };
}
