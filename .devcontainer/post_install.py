#!/usr/bin/env python3
"""Post-installation setup script for devcontainer."""

import os
import subprocess
import sys
from pathlib import Path


def run_command(cmd: list[str], check: bool = True) -> subprocess.CompletedProcess:
    """Run a shell command and return the result."""
    print(f"Running: {' '.join(cmd)}")
    return subprocess.run(cmd, check=check, capture_output=True, text=True)


def setup_git_config():
    """Configure git settings if not already set."""
    try:
        # Check if user.name is set
        result = run_command(["git", "config", "--global", "user.name"], check=False)
        if not result.stdout.strip():
            print("Git user.name not set, configuring default...")
            run_command(["git", "config", "--global", "user.name", "DevContainer User"])
        
        # Check if user.email is set
        result = run_command(["git", "config", "--global", "user.email"], check=False)
        if not result.stdout.strip():
            print("Git user.email not set, configuring default...")
            run_command(["git", "config", "--global", "user.email", "devcontainer@example.com"])
        
        # Set up git delta as pager
        run_command(["git", "config", "--global", "core.pager", "delta"])
        run_command(["git", "config", "--global", "interactive.diffFilter", "delta --color-only"])
        run_command(["git", "config", "--global", "delta.navigate", "true"])
        run_command(["git", "config", "--global", "merge.conflictstyle", "diff3"])
        run_command(["git", "config", "--global", "diff.colorMoved", "default"])
        
        print("✓ Git configuration complete")
    except subprocess.CalledProcessError as e:
        print(f"Warning: Git configuration failed: {e}", file=sys.stderr)


def main():
    """Main setup routine."""
    print("=" * 60)
    print("DevContainer Post-Installation Setup")
    print("=" * 60)
    
    setup_git_config()
    
    print("=" * 60)
    print("✓ Post-installation complete!")
    print("=" * 60)


if __name__ == "__main__":
    main()
