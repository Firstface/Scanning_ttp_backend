import http from 'k6/http';
import { check, sleep } from 'k6';
import { Trend } from 'k6/metrics';

export const options = {
  vus: 50,
  duration: '60s',
  thresholds: {
    http_req_failed: ['rate<0.01'],
    checks: ['rate>0.99'],
    http_req_duration: ['p(95)<2000'],
    health_request_duration: ['p(95)<500'],
    create_task_request_duration: ['p(95)<2000'],
  },
};

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';
const healthRequestDuration = new Trend('health_request_duration', true);
const createTaskRequestDuration = new Trend('create_task_request_duration', true);

export default function () {
  let response = http.get(`${BASE_URL}/actuator/health`);
  healthRequestDuration.add(response.timings.duration);
  check(response, {
    'health is UP': (r) => r.status === 200,
  });
  sleep(0.1);
  if (__VU % 3 === 0) {
    const payload = JSON.stringify({databaseName: 'demo_db', tableName: 'demo_table', targetSampleRows: 1000, selectedPartitions: ['2026-05-01', '2026-05-02']});
    response = http.post(`${BASE_URL}/api/sample-tasks`, payload, {headers: {'Content-Type': 'application/json'}});
    createTaskRequestDuration.add(response.timings.duration);
    check(response, {
      'create task status is 201': (r) => r.status === 201,
    });
  }
}
