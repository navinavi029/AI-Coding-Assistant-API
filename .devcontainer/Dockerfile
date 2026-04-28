FROM mcr.microsoft.com/devcontainers/base:ubuntu-24.04

# Install system dependencies
RUN apt-get update && export DEBIAN_FRONTEND=noninteractive \
    && apt-get install -y --no-install-recommends \
    curl \
    ca-certificates \
    git \
    build-essential \
    ripgrep \
    fd-find \
    fzf \
    tmux \
    git-delta \
    iptables \
    ipset \
    && rm -rf /var/lib/apt/lists/*

# Install uv (Python package manager)
COPY --from=ghcr.io/astral-sh/uv:latest /uv /uvx /bin/

# Install Python 3.13 via uv (fast binary download)
RUN uv python install 3.13 --default

# Install fnm (Fast Node Manager)
RUN curl -fsSL https://fnm.vercel.app/install | bash -s -- --skip-shell

# Install Node 22 via fnm
RUN bash -c "source /root/.bashrc && fnm install 22 && fnm default 22"

# Install ast-grep
RUN curl -fsSL https://github.com/ast-grep/ast-grep/releases/latest/download/ast-grep-x86_64-unknown-linux-gnu.zip -o /tmp/ast-grep.zip \
    && unzip /tmp/ast-grep.zip -d /tmp \
    && mv /tmp/ast-grep /usr/local/bin/ \
    && chmod +x /usr/local/bin/ast-grep \
    && rm /tmp/ast-grep.zip

# Copy post-install script
COPY post_install.py /opt/post_install.py
RUN chmod +x /opt/post_install.py

# Set up vscode user
USER vscode
WORKDIR /workspace

# Configure fnm for vscode user
RUN curl -fsSL https://fnm.vercel.app/install | bash -s -- --skip-shell
RUN bash -c "source ~/.bashrc && fnm install 22 && fnm default 22"

# Copy shell configuration
COPY .zshrc /home/vscode/.zshrc
