# Enable Powerlevel10k instant prompt
if [[ -r "${XDG_CACHE_HOME:-$HOME/.cache}/p10k-instant-prompt-${(%):-%n}.zsh" ]]; then
  source "${XDG_CACHE_HOME:-$HOME/.cache}/p10k-instant-prompt-${(%):-%n}.zsh"
fi

# Path configuration
export PATH="$HOME/.local/bin:$PATH"

# fnm (Fast Node Manager)
eval "$(fnm env --use-on-cd)"

# Aliases
alias ll='ls -lah'
alias g='git'
alias gs='git status'
alias gd='git diff'
alias gc='git commit'
alias gp='git push'
alias gl='git log --oneline --graph --decorate'

# fzf configuration
[ -f ~/.fzf.zsh ] && source ~/.fzf.zsh
export FZF_DEFAULT_COMMAND='fd --type f --hidden --follow --exclude .git'
export FZF_CTRL_T_COMMAND="$FZF_DEFAULT_COMMAND"

# History configuration
HISTFILE=~/.zsh_history
HISTSIZE=10000
SAVEHIST=10000
setopt SHARE_HISTORY
setopt HIST_IGNORE_DUPS
setopt HIST_IGNORE_SPACE

# Maven aliases
alias mvnci='mvn clean install'
alias mvncp='mvn clean package'
alias mvnt='mvn test'
alias mvnboot='mvn spring-boot:run'

# Welcome message
echo "🚀 Multi-Agent Coding Assistant DevContainer"
echo "Java 17 | Maven | Spring Boot 3.2.1"
