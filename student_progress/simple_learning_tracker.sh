#!/bin/bash

# 🎯 Simple Learning Tracker - Start Here!
# Usage: ./simple_learning_tracker.sh [task_name]
# Example: ./simple_learning_tracker.sh "HashMap Implementation"

# Colors for better output
RED='\033[0;31m'
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
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
    echo "Date,Time,Task,Duration_Minutes,Quality_Self_Rating,Difficulty_Rating,Completion_Status,Notes" > "$LOG_FILE"
fi

# Function to start learning session
start_session() {
    local task_name="$1"
    
    echo -e "${BLUE}🚀 Starting Learning Session${NC}"
    echo -e "${YELLOW}📚 Task: $task_name${NC}"
    echo -e "${YELLOW}⏰ Start Time: $TIME${NC}"
    echo ""
    
    # Save session start info
    echo "$TIMESTAMP" > learning_data/session_start.tmp
    echo "$task_name" > learning_data/current_task.tmp
    
    echo -e "${GREEN}✅ Session started! Focus and code well!${NC}"
    echo -e "${BLUE}💡 Tip: Run './simple_learning_tracker.sh finish' when you're done${NC}"
}

# Function to finish learning session
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
    
    echo -e "${BLUE}🎯 Learning Session Complete!${NC}"
    echo -e "${YELLOW}📚 Task: $task_name${NC}"
    echo -e "${YELLOW}⏱️  Duration: ${duration_minutes} minutes${NC}"
    echo ""
    
    # Get user feedback
    echo -e "${BLUE}📝 Quick Reflection (helps improve your learning):${NC}"
    echo ""
    
    read -p "🎯 Quality of your work (1-10): " quality_rating
    read -p "🧠 Task difficulty (1-10): " difficulty_rating
    read -p "✅ Completion status (completed/partial/struggling): " completion_status
    read -p "💭 What you learned or struggled with: " notes
    
    # Save to CSV log
    echo "$DATE,$TIME,$task_name,$duration_minutes,$quality_rating,$difficulty_rating,$completion_status,$notes" >> "$LOG_FILE"
    
    # Create daily log entry
    cat >> "$DAILY_LOG" << EOF

## 📚 Session: $task_name
- **Time**: $TIME (${duration_minutes} minutes)
- **Quality Rating**: $quality_rating/10
- **Difficulty**: $difficulty_rating/10
- **Status**: $completion_status
- **Notes**: $notes

EOF
    
    # Clean up temp files
    rm -f learning_data/session_start.tmp learning_data/current_task.tmp
    
    echo ""
    echo -e "${GREEN}✅ Session logged successfully!${NC}"
    echo -e "${BLUE}📊 Run './simple_learning_tracker.sh report' to see your progress${NC}"
}

# Function to generate progress report
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
    
    # Recent streak
    recent_dates=$(tail -n +2 "$LOG_FILE" | cut -d',' -f1 | sort -u | tail -7)
    streak_days=$(echo "$recent_dates" | wc -l)
    
    echo ""
    echo -e "${YELLOW}🎯 Overall Progress:${NC}"
    echo "   📈 Total Sessions: $total_sessions"
    echo "   ⏰ Total Learning Time: $total_minutes minutes ($(($total_minutes / 60))h $(($total_minutes % 60))m)"
    echo "   🎯 Average Quality Score: $(printf "%.1f" $avg_quality)/10"
    echo "   📅 This Week: $this_week_sessions sessions"
    echo "   🔥 Recent Activity: $streak_days days"
    
    echo ""
    echo -e "${YELLOW}📊 Recent Sessions:${NC}"
    tail -n 5 "$LOG_FILE" | while IFS=',' read -r date time task duration quality difficulty status notes; do
        if [[ "$date" != "Date" ]]; then
            echo "   • $date: $task (${duration}min, Quality: ${quality}/10)"
        fi
    done
    
    echo ""
    echo -e "${YELLOW}💡 Learning Insights:${NC}"
    
    # Find most challenging tasks
    challenging_tasks=$(tail -n +2 "$LOG_FILE" | awk -F',' '$6 >= 8 {print $3}' | sort | uniq -c | sort -nr | head -3)
    if [[ -n "$challenging_tasks" ]]; then
        echo "   🧠 Most Challenging Topics:"
        echo "$challenging_tasks" | while read count task; do
            echo "      • $task ($count times)"
        done
    fi
    
    # Find high-quality work
    quality_work=$(tail -n +2 "$LOG_FILE" | awk -F',' '$5 >= 8 {print $3}' | sort | uniq -c | sort -nr | head -3)
    if [[ -n "$quality_work" ]]; then
        echo "   ⭐ High Quality Work:"
        echo "$quality_work" | while read count task; do
            echo "      • $task ($count times)"
        done
    fi
    
    echo ""
    echo -e "${BLUE}🎯 Recommendations:${NC}"
    
    # Generate simple recommendations
    if (( $(echo "$avg_quality < 7" | bc -l) )); then
        echo "   📈 Focus on improving code quality - take more time for review"
    fi
    
    if [[ $this_week_sessions -lt 3 ]]; then
        echo "   📅 Try to maintain at least 3 learning sessions per week"
    fi
    
    recent_completion=$(tail -n 3 "$LOG_FILE" | cut -d',' -f7 | grep -c "struggling")
    if [[ $recent_completion -gt 1 ]]; then
        echo "   💪 Consider reviewing fundamentals or seeking help with recent topics"
    fi
    
    echo ""
    echo -e "${GREEN}✅ Keep up the great work! Every session counts! 🚀${NC}"
}

# Function to show learning calendar
show_calendar() {
    echo -e "${BLUE}📅 Your Learning Calendar${NC}"
    echo "═══════════════════════════════════════════════════════"
    
    if [[ ! -f "$LOG_FILE" ]]; then
        echo -e "${RED}❌ No learning data found.${NC}"
        exit 1
    fi
    
    # Show last 14 days
    for i in {13..0}; do
        check_date=$(date -d "$i days ago" '+%Y-%m-%d')
        day_name=$(date -d "$i days ago" '+%a')
        
        sessions=$(tail -n +2 "$LOG_FILE" | grep "^$check_date" | wc -l)
        total_time=$(tail -n +2 "$LOG_FILE" | grep "^$check_date" | cut -d',' -f4 | awk '{sum+=$1} END {print sum}')
        
        if [[ $sessions -gt 0 ]]; then
            echo -e "${GREEN}✅ $check_date ($day_name): $sessions sessions, ${total_time}min${NC}"
        else
            echo -e "${RED}❌ $check_date ($day_name): No learning${NC}"
        fi
    done
    
    echo ""
    echo -e "${BLUE}💡 Consistency is key to mastering programming concepts!${NC}"
}

# Main script logic
case "${1:-help}" in
    "finish"|"done"|"complete")
        finish_session
        ;;
    "report"|"stats"|"progress")
        generate_report
        ;;
    "calendar"|"streak"|"history")
        show_calendar
        ;;
    "help"|"--help"|"-h")
        echo -e "${BLUE}🎯 Simple Learning Tracker Usage:${NC}"
        echo ""
        echo "Start a session:    ./simple_learning_tracker.sh \"Task Name\""
        echo "Finish session:     ./simple_learning_tracker.sh finish"
        echo "View progress:      ./simple_learning_tracker.sh report"
        echo "View calendar:      ./simple_learning_tracker.sh calendar"
        echo "Show help:          ./simple_learning_tracker.sh help"
        echo ""
        echo -e "${YELLOW}Examples:${NC}"
        echo "  ./simple_learning_tracker.sh \"HashMap Implementation\""
        echo "  ./simple_learning_tracker.sh \"Volatile Memory Model\""
        echo "  ./simple_learning_tracker.sh \"Thread Pool Design\""
        ;;
    *)
        start_session "$1"
        ;;
esac