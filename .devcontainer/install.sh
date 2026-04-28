#!/bin/bash
# DevContainer CLI Helper - install.sh
# Provides the 'devc' command for managing devcontainers

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

print_usage() {
    cat << EOF
DevContainer CLI Helper

Usage: devc <command>

Commands:
    up          Build and start the devcontainer
    down        Stop and remove the devcontainer
    rebuild     Rebuild the devcontainer from scratch
    shell       Open a shell in the running devcontainer
    logs        Show devcontainer logs
    status      Show devcontainer status

Self-install:
    Run: .devcontainer/install.sh self-install
    This adds the 'devc' command to your PATH
EOF
}

devc_up() {
    echo -e "${GREEN}Starting devcontainer...${NC}"
    devcontainer up --workspace-folder "$PROJECT_ROOT"
}

devc_down() {
    echo -e "${YELLOW}Stopping devcontainer...${NC}"
    docker compose -f "$PROJECT_ROOT/.devcontainer/docker-compose.yml" down 2>/dev/null || true
}

devc_rebuild() {
    echo -e "${YELLOW}Rebuilding devcontainer...${NC}"
    devcontainer up --workspace-folder "$PROJECT_ROOT" --remove-existing-container
}

devc_shell() {
    echo -e "${GREEN}Opening shell in devcontainer...${NC}"
    devcontainer exec --workspace-folder "$PROJECT_ROOT" /bin/zsh
}

devc_logs() {
    docker compose -f "$PROJECT_ROOT/.devcontainer/docker-compose.yml" logs -f
}

devc_status() {
    echo -e "${GREEN}DevContainer Status:${NC}"
    docker compose -f "$PROJECT_ROOT/.devcontainer/docker-compose.yml" ps
}

self_install() {
    local install_dir="$HOME/.local/bin"
    local devc_path="$install_dir/devc"
    
    mkdir -p "$install_dir"
    
    cat > "$devc_path" << 'DEVC_SCRIPT'
#!/bin/bash
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
exec "$SCRIPT_DIR/../../.devcontainer/install.sh" "$@"
DEVC_SCRIPT
    
    chmod +x "$devc_path"
    
    echo -e "${GREEN}✓ 'devc' command installed to $devc_path${NC}"
    echo -e "${YELLOW}Make sure $install_dir is in your PATH${NC}"
    echo ""
    echo "Add this to your ~/.bashrc or ~/.zshrc:"
    echo "  export PATH=\"\$HOME/.local/bin:\$PATH\""
}

# Main command router
case "${1:-}" in
    up)
        devc_up
        ;;
    down)
        devc_down
        ;;
    rebuild)
        devc_rebuild
        ;;
    shell)
        devc_shell
        ;;
    logs)
        devc_logs
        ;;
    status)
        devc_status
        ;;
    self-install)
        self_install
        ;;
    *)
        print_usage
        exit 1
        ;;
esac
