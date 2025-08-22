#!/bin/bash

# Progress Update Script
# Usage: ./update_progress.sh TASK_ID (e.g., 1.1.5)
# This script updates both MICRO_TASK_C01.md and PROGRESS.md when tasks are completed

TASK_ID="$1"

if [ -z "$TASK_ID" ]; then
    echo "Usage: $0 TASK_ID"
    echo "Example: $0 1.1.5"
    exit 1
fi

# Extract chapter number from task ID
CHAPTER=$(echo "$TASK_ID" | cut -d'.' -f1)

# Define file paths
MICRO_TASK_FILE="../micro_tasks/MICRO_TASK_C${CHAPTER}.md"
PROGRESS_FILE="../PROGRESS.md"

# Function to update checkboxes in a file
update_checkboxes() {
    local file="$1"
    local task_id="$2"
    
    if [ ! -f "$file" ]; then
        echo "Error: File $file not found!"
        return 1
    fi
    
    # Create temporary file
    local temp_file=$(mktemp)
    
    # Update all occurrences of the task in all difficulty levels
    sed "s/- \[ \] \*\*Task $task_id\*\*/🦄 **Task $task_id**/g" "$file" > "$temp_file"
    
    # Move temp file back to original
    mv "$temp_file" "$file"
    
    echo "✅ Updated Task $task_id in $file"
}

# Function to log completion
log_completion() {
    local task_id="$1"
    local timestamp=$(date '+%Y-%m-%d %H:%M:%S')
    echo "[$timestamp] Completed Task $task_id" >> "learning_data/completion_log.txt"
}

echo "🎯 Updating progress for Task $TASK_ID..."

# Update MICRO_TASK file
update_checkboxes "$MICRO_TASK_FILE" "$TASK_ID"

# Update PROGRESS.md
update_checkboxes "$PROGRESS_FILE" "$TASK_ID"

# Log the completion
log_completion "$TASK_ID"

echo "🎉 Progress tracking completed for Task $TASK_ID!"
echo "📊 Both $MICRO_TASK_FILE and $PROGRESS_FILE have been updated."