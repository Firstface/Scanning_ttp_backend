const { test, expect } = require('@playwright/test');

test.describe('Hive Sampling API E2E Tests', () => {
  
  test('Health check should work', async ({ request }) => {
    const response = await request.get('/actuator/health');
    expect(response.ok()).toBeTruthy();
    const data = await response.json();
    expect(data.status).toBe('UP');
  });

  test('Create and retrieve sample task', async ({ request }) => {
    // 1. Create task
    const createResponse = await request.post('/api/sample-tasks', {
      data: {
        databaseName: 'demo_db',
        tableName: 'demo_table',
        targetSampleRows: 2500,
        selectedPartitions: ['2026-05-01', '2026-05-02', '2026-05-03']
      }
    });
    expect(createResponse.ok()).toBeTruthy();
    const task = await createResponse.json();
    const taskId = task.taskId;
    expect(taskId).toBeDefined();

    // 2. Get task details
    const getResponse = await request.get(`/api/sample-tasks/${taskId}`);
    expect(getResponse.ok()).toBeTruthy();
    const taskDetails = await getResponse.json();
    expect(taskDetails.taskId).toBe(taskId);

    // 3. Wait for pipeline and check pipeline
    await new Promise(resolve => setTimeout(resolve, 20000));
    const pipelineResponse = await request.get(`/api/sample-tasks/${taskId}/pipeline`);
    expect(pipelineResponse.ok()).toBeTruthy();
    const pipeline = await pipelineResponse.json();
    expect(pipeline.executors).toBeDefined();
  });

  test('List all tasks should work', async ({ request }) => {
    const response = await request.get('/api/sample-tasks');
    expect(response.ok()).toBeTruthy();
    const tasks = await response.json();
    expect(Array.isArray(tasks)).toBeTruthy();
  });
});
