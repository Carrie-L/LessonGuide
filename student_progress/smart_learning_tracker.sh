#!/bin/bash

# 🎯 Smart Learning Tracker - Enhanced with Task Browser
# Usage: ./smart_learning_tracker.sh (interactive mode)
# Or: ./smart_learning_tracker.sh "specific task name"

# Colors for better output
RED='\033[0;31m'
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
CYAN='\033[0;36m'
PURPLE='\033[0;35m'
NC='\033[0m' # No Color

# Create directories if they don't exist
mkdir -p learning_data
mkdir -p daily_reports

# Get current date and time
DATE=$(date '+%Y-%m-%d')
TIME=$(date '+%H:%M')
TIMESTAMP=$(date '+%s')

# Learning data files
LOG_FILE="learning_data/learning_log.csv"
DAILY_LOG="learning_data/daily_${DATE}.md"
PROGRESS_FILE="learning_data/progress_summary.md"

# Initialize log file if it doesn't exist
if [[ ! -f "$LOG_FILE" ]]; then
    echo "Date,Time,Task,Duration_Minutes,Quality_Self_Rating,Difficulty_Rating,Completion_Status,Notes,Task_File,Task_ID" > "$LOG_FILE"
fi

# Function to scan and display available tasks
show_available_tasks() {
    echo -e "${BLUE}📚 Available Learning Tasks${NC}"
    echo "═══════════════════════════════════════════════════════"
    
    # Look for micro task files in the micro_tasks directory
    local micro_tasks_dir="../micro_tasks"
    local task_counter=1
    
    # Store tasks in arrays for selection
    declare -a task_files=()
    declare -a task_names=()
    declare -a task_ids=()
    
    echo -e "${YELLOW}🔍 Scanning micro task files...${NC}"
    echo ""
    
    # Chapter 1: Concurrency Programming
    if [[ -f "$micro_tasks_dir/MICRO_TASK_C01_C02.md" ]]; then
        echo -e "${CYAN}📖 Chapter 1: 并发编程 (Concurrency Programming)${NC}"
        
        # Extract tasks from C01_C02 file
        grep -n "^### Task 1\." "$micro_tasks_dir/MICRO_TASK_C01_C02.md" 2>/dev/null | while read line; do
            task_line=$(echo "$line" | cut -d: -f2- | sed 's/^### //')
            task_id=$(echo "$task_line" | grep -o "Task 1\.[0-9]\+\.[0-9]\+" | head -1)
            task_desc=$(echo "$task_line" | sed "s/Task 1\.[0-9]\+\.[0-9]\+[^:]*: *//" | cut -c1-60)
            
            if [[ -n "$task_id" && -n "$task_desc" ]]; then
                printf "${GREEN}%2d.${NC} %s: %s...\n" $task_counter "$task_id" "$task_desc"
                task_files[$task_counter]="$micro_tasks_dir/MICRO_TASK_C01_C02.md"
                task_names[$task_counter]="$task_id: $task_desc"
                task_ids[$task_counter]="$task_id"
                ((task_counter++))
                
                if [[ $task_counter -gt 15 ]]; then
                    break
                fi
            fi
        done
    fi
    
    # Chapter 3: Architecture Patterns  
    if [[ -f "$micro_tasks_dir/MICRO_TASK_C03.md" ]]; then
        echo ""
        echo -e "${CYAN}📖 Chapter 3: 架构模式 (Architecture Patterns)${NC}"
        
        grep -n "^### Task 3\." "$micro_tasks_dir/MICRO_TASK_C03.md" 2>/dev/null | head -8 | while read line; do
            task_line=$(echo "$line" | cut -d: -f2- | sed 's/^### //')
            task_id=$(echo "$task_line" | grep -o "Task 3\.[0-9]\+\.[0-9]\+" | head -1)
            task_desc=$(echo "$task_line" | sed "s/Task 3\.[0-9]\+\.[0-9]\+[^:]*: *//" | cut -c1-60)
            
            if [[ -n "$task_id" && -n "$task_desc" ]]; then
                printf "${GREEN}%2d.${NC} %s: %s...\n" $task_counter "$task_id" "$task_desc"
                ((task_counter++))
            fi
        done
    fi
    
    # Chapter 12: HTTP Protocol
    if [[ -f "$micro_tasks_dir/ENHANCED_MICRO_TASKS_C12_HTTP.md" ]]; then
        echo ""
        echo -e "${CYAN}📖 Chapter 12: HTTP协议 (HTTP Protocol)${NC}"
        
        grep -n "^### Task 12\." "$micro_tasks_dir/ENHANCED_MICRO_TASKS_C12_HTTP.md" 2>/dev/null | head -8 | while read line; do
            task_line=$(echo "$line" | cut -d: -f2- | sed 's/^### //')
            task_id=$(echo "$task_line" | grep -o "Task 12\.[0-9]\+\.[0-9]\+" | head -1)
            task_desc=$(echo "$task_line" | sed "s/Task 12\.[0-9]\+\.[0-9]\+[^:]*: *//" | cut -c1-60)
            
            if [[ -n "$task_id" && -n "$task_desc" ]]; then
                printf "${GREEN}%2d.${NC} %s: %s...\n" $task_counter "$task_id" "$task_desc"
                ((task_counter++))
            fi
        done
    fi
    
    echo ""
    echo -e "${PURPLE}📋 Custom Tasks:${NC}"
    printf "${GREEN}%2d.${NC} Custom Task (Enter your own task name)\n" $task_counter
    printf "${GREEN}%2d.${NC} Browse Files (Open specific micro task file)\n" $((task_counter + 1))
    
    echo ""
}

# Function to browse and select specific task file
browse_task_files() {
    echo -e "${BLUE}📁 Available Micro Task Files${NC}"
    echo "═══════════════════════════════════════════════════════"
    
    local micro_tasks_dir="../micro_tasks"
    local file_counter=1
    declare -a available_files=()
    
    # List all micro task files
    for file in "$micro_tasks_dir"/*.md; do
        if [[ -f "$file" ]]; then
            filename=$(basename "$file")
            case "$filename" in
                "MICRO_TASK_C01_C02.md")
                    printf "${GREEN}%d.${NC} ${CYAN}%s${NC} - 并发编程 & 集合框架\n" $file_counter "$filename"
                    ;;
                "MICRO_TASK_C03.md") 
                    printf "${GREEN}%d.${NC} ${CYAN}%s${NC} - 架构模式\n" $file_counter "$filename"
                    ;;
                "MICRO_TASK_C06.md")
                    printf "${GREEN}%d.${NC} ${CYAN}%s${NC} - 测试框架\n" $file_counter "$filename"
                    ;;
                "MICRO_TASK_C07_C08.md")
                    printf "${GREEN}%d.${NC} ${CYAN}%s${NC} - 安全防护 & 工程效能\n" $file_counter "$filename"
                    ;;
                "MICRO_TASKS_C09.md")
                    printf "${GREEN}%d.${NC} ${CYAN}%s${NC} - 系统设计\n" $file_counter "$filename"
                    ;;
                "ENHANCED_MICRO_TASKS_C12_HTTP.md")
                    printf "${GREEN}%d.${NC} ${CYAN}%s${NC} - HTTP协议深度实践\n" $file_counter "$filename"
                    ;;
                *)
                    printf "${GREEN}%d.${NC} ${CYAN}%s${NC}\n" $file_counter "$filename"
                    ;;
            esac
            available_files[$file_counter]="$file"
            ((file_counter++))
        fi
    done
    
    echo ""
    read -p "📖 Select file number to open (or Enter to go back): " file_choice
    
    if [[ -n "$file_choice" && "$file_choice" -ge 1 && "$file_choice" -lt $file_counter ]]; then
        selected_file="${available_files[$file_choice]}"
        echo -e "${BLUE}📖 Opening: $(basename "$selected_file")${NC}"
        
        # Show file content with task highlighting
        echo "═══════════════════════════════════════════════════════"
        
        # Extract and display tasks from the selected file
        echo -e "${YELLOW}Available tasks in this file:${NC}"
        echo ""
        
        grep -n "^### Task" "$selected_file" | head -20 | while read line; do
            line_num=$(echo "$line" | cut -d: -f1)
            task_text=$(echo "$line" | cut -d: -f2- | sed 's/^### //')
            printf "${GREEN}Line %s:${NC} %s\n" "$line_num" "$task_text"
        done
        
        echo ""
        read -p "📝 Enter the exact task name you want to work on: " selected_task
        
        if [[ -n "$selected_task" ]]; then
            start_session "$selected_task" "$selected_file" "custom"
        fi
    fi
}

# Interactive task selection
interactive_task_selection() {
    clear
    echo -e "${BLUE}🎯 Smart Learning Tracker - Task Selection${NC}"
    echo "═══════════════════════════════════════════════════════"
    echo ""
    
    show_available_tasks
    
    echo -e "${YELLOW}💡 Quick Options:${NC}"
    echo "   • Enter task number to start immediately"
    echo "   • Type 'browse' to explore specific files"
    echo "   • Type 'recent' to see your recent tasks"
    echo "   • Type 'help' for more options"
    echo ""
    
    read -p "🎯 Select your choice (number/command): " choice
    
    case "$choice" in
        "browse")
            browse_task_files
            ;;
        "recent")
            show_recent_tasks
            ;;
        "help")
            show_help
            ;;
        [0-9]*)
            # Handle numeric selection
            if [[ "$choice" -eq $((task_counter - 1)) ]]; then
                # Custom task option
                read -p "📝 Enter your custom task name: " custom_task
                if [[ -n "$custom_task" ]]; then
                    start_session "$custom_task" "custom" "custom"
                fi
            elif [[ "$choice" -eq $task_counter ]]; then
                # Browse files option
                browse_task_files
            else
                echo -e "${RED}❌ Task selection not implemented yet. Please enter custom task name.${NC}"
                read -p "📝 Enter task name: " custom_task
                if [[ -n "$custom_task" ]]; then
                    start_session "$custom_task" "auto-selected" "$choice"
                fi
            fi
            ;;
        *)
            echo -e "${RED}❌ Invalid choice. Please try again.${NC}"
            sleep 2
            interactive_task_selection
            ;;
    esac
}

# Function to show recent tasks
show_recent_tasks() {
    echo -e "${BLUE}📋 Your Recent Tasks${NC}"
    echo "═══════════════════════════════════════════════════════"
    
    if [[ ! -f "$LOG_FILE" ]]; then
        echo -e "${RED}❌ No recent tasks found. Start your first session!${NC}"
        return
    fi
    
    echo -e "${YELLOW}Last 10 tasks:${NC}"
    tail -n 10 "$LOG_FILE" | while IFS=',' read -r date time task duration quality difficulty status notes file_path task_id; do
        if [[ "$date" != "Date" ]]; then
            printf "${GREEN}•${NC} %s (%s) - Quality: %s/10, Duration: %smin\n" "$task" "$date" "$quality" "$duration"
        fi
    done
    
    echo ""
    read -p "📝 Enter task name to start (or Enter to go back): " selected_task
    if [[ -n "$selected_task" ]]; then
        start_session "$selected_task" "recent" "recent"
    else
        interactive_task_selection
    fi
}

# Function to start learning session
start_session() {
    local task_name="$1"
    local task_file="${2:-unknown}"
    local task_id="${3:-unknown}"
    
    clear
    echo -e "${BLUE}🚀 Starting Learning Session${NC}"
    echo "═══════════════════════════════════════════════════════"
    echo -e "${YELLOW}📚 Task: $task_name${NC}"
    echo -e "${YELLOW}⏰ Start Time: $TIME${NC}"
    echo -e "${YELLOW}📁 Source: $task_file${NC}"
    echo ""
    
    # Save session start info
    echo "$TIMESTAMP" > learning_data/session_start.tmp
    echo "$task_name" > learning_data/current_task.tmp
    echo "$task_file" > learning_data/current_file.tmp
    echo "$task_id" > learning_data/current_id.tmp
    
    # Show helpful information based on task
    echo -e "${CYAN}💡 Session Tips:${NC}"
    
    if [[ "$task_name" == *"HashMap"* ]]; then
        echo "   • Focus on hash collision handling"
        echo "   • Implement dynamic resizing"
        echo "   • Test with different key types"
    elif [[ "$task_name" == *"synchronized"* ]]; then
        echo "   • Understand memory visibility"
        echo "   • Test with multiple threads"
        echo "   • Compare with volatile"
    elif [[ "$task_name" == *"HTTP"* ]]; then
        echo "   • Handle different status codes"
        echo "   • Implement proper error handling"
        echo "   • Test connection timeouts"
    else
        echo "   • Break down the task into smaller steps"
        echo "   • Test your implementation thoroughly"
        echo "   • Document your learning process"
    fi
    
    echo ""
    echo -e "${GREEN}✅ Session started! Focus and code well!${NC}"
    echo -e "${BLUE}💾 Run './smart_learning_tracker.sh finish' when you're done${NC}"
    
    # Show quick reference for the task
    if [[ "$task_file" != "unknown" && "$task_file" != "custom" && "$task_file" != "recent" && -f "$task_file" ]]; then
        echo ""
        echo -e "${PURPLE}📖 Quick Reference:${NC}"
        echo -e "${CYAN}   File: $(basename "$task_file")${NC}"
        echo -e "${CYAN}   Open with: code $(basename "$task_file")${NC}"
        echo ""
    fi
}

# Function to finish learning session (same as before but with enhanced logging)
finish_session() {
    if [[ ! -f "learning_data/session_start.tmp" ]]; then
        echo -e "${RED}❌ No active session found. Start a session first!${NC}"
        exit 1
    fi
    
    # Calculate duration
    start_time=$(cat learning_data/session_start.tmp)
    current_time=$(date '+%s')
    duration_seconds=$((current_time - start_time))
    duration_minutes=$((duration_seconds / 60))
    
    task_name=$(cat learning_data/current_task.tmp)
    task_file=$(cat learning_data/current_file.tmp 2>/dev/null || echo "unknown")
    task_id=$(cat learning_data/current_id.tmp 2>/dev/null || echo "unknown")
    
    clear
    echo -e "${BLUE}🎯 Learning Session Complete!${NC}"
    echo "═══════════════════════════════════════════════════════"
    echo -e "${YELLOW}📚 Task: $task_name${NC}"
    echo -e "${YELLOW}⏱️  Duration: ${duration_minutes} minutes${NC}"
    echo -e "${YELLOW}📁 Source: $(basename "$task_file")${NC}"
    echo ""
    
    # Get user feedback with better prompts
    echo -e "${BLUE}📝 Quick Reflection (helps AI understand your learning):${NC}"
    echo ""
    
    echo -e "${CYAN}🎯 How would you rate the quality of your work?${NC}"
    echo "   1-3: Struggled, basic attempt   4-6: Decent progress   7-10: High quality implementation"
    read -p "Quality (1-10): " quality_rating
    
    echo -e "${CYAN}🧠 How difficult was this task for you?${NC}"
    echo "   1-3: Easy, familiar concepts   4-6: Moderate challenge   7-10: Very challenging"
    read -p "Difficulty (1-10): " difficulty_rating
    
    echo -e "${CYAN}✅ What's your completion status?${NC}"
    echo "   Options: completed, partial, struggling, need-help"
    read -p "Status: " completion_status
    
    echo -e "${CYAN}💭 What did you learn or struggle with? (one line)${NC}"
    read -p "Key insight: " notes
    
    # Save to enhanced CSV log
    echo "$DATE,$TIME,$task_name,$duration_minutes,$quality_rating,$difficulty_rating,$completion_status,$notes,$task_file,$task_id" >> "$LOG_FILE"
    
    # Create daily log entry
    cat >> "$DAILY_LOG" << EOF

## 📚 Session: $task_name
- **Time**: $TIME (${duration_minutes} minutes)
- **Source**: $(basename "$task_file")
- **Quality Rating**: $quality_rating/10
- **Difficulty**: $difficulty_rating/10
- **Status**: $completion_status
- **Key Learning**: $notes

EOF
    
    # Clean up temp files
    rm -f learning_data/session_start.tmp learning_data/current_task.tmp learning_data/current_file.tmp learning_data/current_id.tmp
    
    echo ""
    echo -e "${GREEN}✅ Session logged successfully!${NC}"
    
    # Show next steps based on completion status
    if [[ "$completion_status" == "struggling" || "$completion_status" == "need-help" ]]; then
        echo -e "${YELLOW}💡 Suggested next steps:${NC}"
        echo "   • Review the task requirements again"
        echo "   • Break it down into smaller parts"
        echo "   • Ask AI tutor for specific help"
        echo "   • Check similar examples in other files"
    elif [[ "$completion_status" == "completed" && "$quality_rating" -ge 8 ]]; then
        echo -e "${GREEN}🎉 Excellent work! You're ready for the next challenge!${NC}"
    fi
    
    echo ""
    echo -e "${BLUE}📊 Run './smart_learning_tracker.sh report' to see your progress${NC}"
    echo -e "${BLUE}🎯 Run './smart_learning_tracker.sh' to start your next session${NC}"
}

# Enhanced report function (keeping the same logic as before)
generate_report() {
    echo -e "${BLUE}📊 Your Learning Analytics Report${NC}"
    echo "═══════════════════════════════════════════════════════"
    
    if [[ ! -f "$LOG_FILE" ]]; then
        echo -e "${RED}❌ No learning data found. Start tracking first!${NC}"
        exit 1
    fi
    
    # Basic statistics
    total_sessions=$(tail -n +2 "$LOG_FILE" | wc -l)
    total_minutes=$(tail -n +2 "$LOG_FILE" | cut -d',' -f4 | awk '{sum+=$1} END {print sum}')
    avg_quality=$(tail -n +2 "$LOG_FILE" | cut -d',' -f5 | awk '{sum+=$1; count++} END {print sum/count}')
    
    # This week's data
    current_week=$(date '+%Y-%m-%d' -d 'monday')
    this_week_sessions=$(tail -n +2 "$LOG_FILE" | awk -F',' -v week="$current_week" '$1 >= week' | wc -l)
    
    echo ""
    echo -e "${YELLOW}🎯 Overall Progress:${NC}"
    echo "   📈 Total Sessions: $total_sessions"
    echo "   ⏰ Total Learning Time: $total_minutes minutes ($(($total_minutes / 60))h $(($total_minutes % 60))m)"
    echo "   🎯 Average Quality Score: $(printf "%.1f" $avg_quality)/10"
    echo "   📅 This Week: $this_week_sessions sessions"
    
    echo ""
    echo -e "${YELLOW}📊 Recent Sessions:${NC}"
    tail -n 5 "$LOG_FILE" | while IFS=',' read -r date time task duration quality difficulty status notes file_path task_id; do
        if [[ "$date" != "Date" ]]; then
            echo "   • $date: $task (${duration}min, Quality: ${quality}/10)"
        fi
    done
    
    echo ""
    echo -e "${YELLOW}💡 Learning Insights:${NC}"
    
    # Find struggling areas
    struggling_tasks=$(tail -n +2 "$LOG_FILE" | awk -F',' '$7 == "struggling" || $7 == "need-help" {print $3}' | sort | uniq -c | sort -nr)
    if [[ -n "$struggling_tasks" ]]; then
        echo "   ⚠️  Areas needing attention:"
        echo "$struggling_tasks" | head -3 | while read count task; do
            echo "      • $task ($count times)"
        done
    fi
    
    # Find strengths
    strong_tasks=$(tail -n +2 "$LOG_FILE" | awk -F',' '$5 >= 8 && $7 == "completed" {print $3}' | sort | uniq -c | sort -nr)
    if [[ -n "$strong_tasks" ]]; then
        echo "   ⭐ Your strengths:"
        echo "$strong_tasks" | head -3 | while read count task; do
            echo "      • $task ($count times)"
        done
    fi
    
    echo ""
    echo -e "${GREEN}✅ Keep up the great work! Every session counts! 🚀${NC}"
}

# Show help function
show_help() {
    echo -e "${BLUE}🎯 Smart Learning Tracker - Help${NC}"
    echo "═══════════════════════════════════════════════════════"
    echo ""
    echo -e "${YELLOW}Available Commands:${NC}"
    echo ""
    echo "🚀 Start Learning:"
    echo "   ./smart_learning_tracker.sh              → Interactive task selection"
    echo "   ./smart_learning_tracker.sh \"task name\"  → Direct task start"
    echo ""
    echo "⏹️  End Session:"
    echo "   ./smart_learning_tracker.sh finish"
    echo "   ./smart_learning_tracker.sh done"
    echo ""
    echo "📊 View Progress:"
    echo "   ./smart_learning_tracker.sh report"
    echo "   ./smart_learning_tracker.sh calendar"
    echo ""
    echo "🔍 Browse Tasks:"
    echo "   ./smart_learning_tracker.sh browse"
    echo "   ./smart_learning_tracker.sh recent"
    echo ""
    echo -e "${CYAN}💡 Pro Tips:${NC}"
    echo "   • Run without arguments for interactive mode"
    echo "   • Use 'recent' to quickly restart previous tasks"
    echo "   • Check 'report' weekly to see your patterns"
    echo ""
}

# Main script logic
case "${1:-interactive}" in
    "finish"|"done"|"complete")
        finish_session
        ;;
    "report"|"stats"|"progress")
        generate_report
        ;;
    "calendar"|"streak"|"history")
        show_calendar
        ;;
    "browse")
        browse_task_files
        ;;
    "recent")
        show_recent_tasks
        ;;
    "help"|"--help"|"-h")
        show_help
        ;;
    "interactive")
        interactive_task_selection
        ;;
    *)
        start_session "$1" "command-line" "direct"
        ;;
esac