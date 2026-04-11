# Security Policy

## Supported Versions

We release security updates for the following versions of the Multi-Agent Coding Assistant:

| Version | Supported          |
| ------- | ------------------ |
| 1.0.x   | :white_check_mark: |
| < 1.0   | :x:                |

## Reporting a Vulnerability

We take the security of the Multi-Agent Coding Assistant seriously. If you discover a security vulnerability, please report it responsibly.

### How to Report

**Please do NOT report security vulnerabilities through public GitHub issues.**

Instead, please report security vulnerabilities using one of the following methods:

1. **GitHub Security Advisories** (Preferred)
   - Navigate to the repository's Security tab
   - Click "Report a vulnerability"
   - Fill out the advisory form with details

2. **Email**
   - Send an email to the project maintainers
   - Include "SECURITY" in the subject line
   - Provide detailed information about the vulnerability

### What to Include

When reporting a vulnerability, please include:

- Type of vulnerability (e.g., injection, authentication bypass, information disclosure)
- Full paths of source file(s) related to the vulnerability
- Location of the affected source code (tag/branch/commit or direct URL)
- Step-by-step instructions to reproduce the issue
- Proof-of-concept or exploit code (if possible)
- Impact of the vulnerability, including how an attacker might exploit it
- Any potential mitigations you've identified

### Response Timeline

- **Initial Response**: We will acknowledge receipt of your vulnerability report within 48 hours
- **Status Updates**: We will provide regular updates on our progress at least every 7 days
- **Resolution Timeline**: We aim to resolve critical vulnerabilities within 30 days of initial report
- **Disclosure**: We will coordinate with you on the disclosure timeline

## Disclosure Policy

We follow a coordinated disclosure process:

1. **Private Disclosure**: You report the vulnerability privately to us
2. **Investigation**: We investigate and develop a fix
3. **Fix Development**: We create and test a security patch
4. **Coordinated Release**: We release the fix and publish a security advisory
5. **Public Disclosure**: After users have had time to update (typically 7-14 days), we publicly disclose the vulnerability details

We request that you:
- Give us reasonable time to investigate and fix the vulnerability before public disclosure
- Make a good faith effort to avoid privacy violations, data destruction, and service disruption
- Do not exploit the vulnerability beyond what is necessary to demonstrate it

## Security Update Process

When a security vulnerability is confirmed:

1. We will develop a fix in a private repository fork
2. We will create a new release with the security patch
3. We will publish a GitHub Security Advisory with:
   - Description of the vulnerability
   - Affected versions
   - Fixed versions
   - Workarounds (if available)
   - Credit to the reporter (if desired)
4. We will update this SECURITY.md file if the vulnerability affects our security practices

## Out of Scope

The following are generally considered out of scope for security reports:

- Vulnerabilities in dependencies (please report these to the respective projects)
- Denial of service attacks requiring excessive resources
- Issues requiring physical access to a user's device
- Social engineering attacks
- Vulnerabilities affecting outdated or unsupported versions
- Issues that require unlikely user interaction

## Security Best Practices

When deploying the Multi-Agent Coding Assistant, we recommend:

- Keep your NVIDIA API key secure and never commit it to version control
- Use environment variables for all sensitive configuration
- Run the application with minimal required permissions
- Keep dependencies up to date
- Use HTTPS for all API communications
- Implement rate limiting and authentication for production deployments
- Regularly review application logs for suspicious activity
- Follow the principle of least privilege for API access

## Contact

For security-related questions that are not vulnerability reports, please open a regular GitHub issue or discussion.

Thank you for helping keep the Multi-Agent Coding Assistant and its users safe!
