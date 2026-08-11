# OWASP ZAP Baseline (DAST) — Summary

- Generated: 2026-08-09 20:21 CST
- Scanner: OWASP ZAP (ghcr.io/zaproxy/zaproxy:stable) via Docker, zap-baseline.py
- Target: http://deploy-backend-1:8080 (backend on deploy_scanning-network)
- Result: FAIL-NEW 0 · WARN-NEW 1 · PASS 66
- Alerts by risk: High 0 · Medium 0 · Low 0 · Informational 1
- Only alert: "Storable and Cacheable Content" (Informational) on /robots.txt & /sitemap.xml (both 404)
- Reports: zap-baseline-report.html / .json / .md
