#!/bin/bash

# Progress Updater Integration Script
# This script contains the checkbox update functionality that can be integrated with functional_ai_assistant.sh
# Called when tasks are completed to update both MICRO_TASK_C*.md and PROGRESS.md files

# Color definitions
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[0;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

# Function to update task completion status in markdown files
update_task_completion() {
    local task_id="$1"
    local task_file="$2"
    
    if [[ -z "$task_id" || "$task_id" == "Unknown" ]]; then
        echo -e "${YELLOW}⚠️ Task ID not found, skipping progress update${NC}"
        return 0
    fi
    
    # Extract chapter number from task ID (e.g., "1.1.5" -> "01")
    local chapter_num=$(echo "$task_id" | sed 's/Task //' | cut -d'.' -f1)
    chapter_num=$(printf "%02d" "$chapter_num")
    
    # Define file paths
    local micro_task_path="../micro_tasks/MICRO_TASK_C${chapter_num}.md"
    local progress_path="../PROGRESS.md"
    
    echo -e "${BLUE}🔄 Updating progress for Task $task_id...${NC}"
    
    # Function to update checkboxes in a specific file
    update_checkboxes_in_file() {
        local file="$1"
        local task_pattern="$2"
        
        if [[ ! -f "$file" ]]; then
            echo -e "${RED}❌ File not found: $file${NC}"
            return 1
        fi
        
        # Create backup
        cp "$file" "${file}.backup_$(date +%s)"
        
        # Update task completion - handle multiple patterns
        local temp_file=$(mktemp)
        
        # Pattern 1: Standard checkbox format
        sed "s/- \\[ \\] \\*\\*$task_pattern\\*\\*/✅ **$task_pattern**/g" "$file" > "$temp_file"
        
        # Pattern 2: Already has emoji but wrong checkbox
        sed -i "s/✅ \\[ \\] \\*\\*$task_pattern\\*\\*/✅ **$task_pattern**/g" "$temp_file"
        
        # Pattern 3: Nested task items (with indentation)
        sed -i "s/  - \\[ \\] \\*\\*$task_pattern\\*\\*/✅ **$task_pattern**/g" "$temp_file" 
        sed -i "s/    - \\[ \\] \\*\\*$task_pattern\\*\\*/✅ **$task_pattern**/g" "$temp_file"
        
        # Check if any changes were made
        if ! diff -q "$file" "$temp_file" > /dev/null; then
            mv "$temp_file" "$file"
            echo -e "${GREEN}✅ Updated checkboxes in $(basename "$file")${NC}"
            return 0
        else
            rm "$temp_file"
            echo -e "${YELLOW}⚠️ No matching checkboxes found for Task $task_pattern in $(basename "$file")${NC}"
            return 1
        fi
    }
    
    # Clean up task_id format (remove "Task " prefix if present)
    local clean_task_id=$(echo "$task_id" | sed 's/^Task //')
    
    # Update both files
    local micro_updated=false
    local progress_updated=false
    
    # Update MICRO_TASK file
    if update_checkboxes_in_file "$micro_task_path" "Task $clean_task_id"; then
        micro_updated=true
    fi
    
    # Update PROGRESS.md file
    if update_checkboxes_in_file "$progress_path" "Task $clean_task_id"; then
        progress_updated=true
    fi
    
    # Summary
    if [[ "$micro_updated" == true || "$progress_updated" == true ]]; then
        echo -e "${GREEN}🎉 Progress tracking updated successfully for Task $clean_task_id!${NC}"
        
        # Log the update
        local timestamp=$(date '+%Y-%m-%d %H:%M:%S')
        echo "[$timestamp] AUTO-UPDATED: Task $clean_task_id progress tracking" >> "learning_data/progress_updates.log"
        
        return 0
    else
        echo -e "${YELLOW}⚠️ No progress updates were made for Task $clean_task_id${NC}"
        return 1
    fi
}

# Function to check and update task completion statistics
update_completion_stats() {
    local task_id="$1"
    
    # Update statistics in performance results
    local stats_file="learning_data/performance_results.json"
    local timestamp=$(date -Iseconds)
    
    # Create basic stats entry if file doesn't exist
    if [[ ! -f "$stats_file" ]]; then
        mkdir -p "learning_data"
        cat > "$stats_file" << EOF
{
  "last_updated": "$timestamp",
  "completed_tasks": ["$task_id"],
  "total_completed": 1,
  "current_chapter": "C01",
  "learning_streak": 1
}
EOF
        echo -e "${GREEN}📊 Created initial statistics file${NC}"
    else
        # Update existing stats (basic implementation - could be enhanced with jq)
        echo -e "${BLUE}📈 Updating completion statistics...${NC}"
        cp "$stats_file" "${stats_file}.backup"
        
        # Simple approach: append to a completion list (would be better with proper JSON parsing)
        local temp_log="learning_data/recent_completions.tmp"
        echo "$(date -Iseconds): $task_id" >> "$temp_log"
    fi
}

# Main function - called from functional_ai_assistant.sh
main() {
    local task_id="$1"
    local task_file="$2"
    
    if [[ -z "$task_id" ]]; then
        echo -e "${RED}❌ Usage: $0 TASK_ID [TASK_FILE]${NC}"
        return 1
    fi
    
    echo -e "${CYAN}🔧 Progress Updater: Synchronizing task completion...${NC}"
    echo "════════════════════════════════════════════════════════"
    
    # Update task completion in markdown files
    update_task_completion "$task_id" "$task_file"
    
    # Update statistics
    update_completion_stats "$task_id"
    
    echo -e "${GREEN}✨ Progress synchronization completed!${NC}"
    echo ""
}

# If script is called directly, run main function
if [[ "${BASH_SOURCE[0]}" == "${0}" ]]; then
    main "$@"
fi