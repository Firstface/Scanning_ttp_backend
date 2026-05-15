# GitHub Setup Checklist

Follow these steps to configure your GitHub repository before the demo.

## 1. Enable GitHub Actions

| Step | Path | Action |
|------|------|--------|
| 1 | Settings → Actions → General | Under "Actions permissions", select **Allow all actions and reusable workflows** |
| 2 | Settings → Actions → General | Under "Workflow permissions", select **Read and write permissions** |
| 3 | Settings → Actions → General | Check **Allow GitHub Actions to create and approve pull requests** |

## 2. Configure Environments

| Step | Path | Action |
|------|------|--------|
| 1 | Settings → Environments | Click **New environment** |
| 2 | Settings → Environments | Name: `staging`, Click **Configure environment** |
| 3 | Settings → Environments | Click **New environment** |
| 4 | Settings → Environments | Name: `production`, Click **Configure environment** |
| 5 | Settings → Environments → production | Under "Environment protection rules", check **Required reviewers** |
| 6 | Settings → Environments → production | Add yourself or the demo team as reviewers |
| 7 | Settings → Environments → production | Save protection rules |

## 3. Secrets Configuration

| Step | Path | Action |
|------|------|--------|
| 1 | Settings → Secrets and variables → Actions | Review the default `GITHUB_TOKEN` - it has permissions for GHCR |
| 2 | Settings → Secrets and variables → Actions | **No additional secrets needed** - all workflows use `GITHUB_TOKEN` |

## 4. GHCR Visibility (after first push)

| Step | Path | Action |
|------|------|--------|
| 1 | Your Profile → Packages | Find `scanning-ttp-backend` package |
| 2 | Package Settings → Change visibility | Change from Private → **Public** |

## 5. Final Verification

- [ ] GitHub Actions enabled for the repo
- [ ] Workflow permissions set to Read & Write
- [ ] `staging` environment created
- [ ] `production` environment created with required reviewers
- [ ] First push completed successfully
- [ ] GHCR package visibility set to Public

## Notes

- The `GITHUB_TOKEN` automatically has permission to push to GHCR when the workflow is triggered
- You need to push at least once to create the GHCR package before you can change its visibility
- Required reviewers will be prompted to approve the production deployment in the Actions UI
