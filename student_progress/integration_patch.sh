#!/bin/bash

# Integration Patch for functional_ai_assistant.sh
# Add this code to your finish_task() function right after line 567 (after the CSV logging)

# ==== ADD THIS CODE TO YOUR finish_task() FUNCTION ====
# Insert after: echo "$completion_date,$completion_time,..." >> "learning_data/learning_log.csv"

echo ""
echo -e "${CYAN}🔄 Auto-updating task progress in markdown files...${NC}"

# Call the progress updater with the extracted task information
if [[ -f "progress_updater.sh" ]]; then
    source "progress_updater.sh"
    update_task_completion "$task_id" "$task_file"
else
    echo -e "${YELLOW}⚠️ Progress updater not found, manual update required${NC}"
fi

echo ""

# ==== END OF INTEGRATION CODE ====

# Instructions:
echo "To integrate with your functional_ai_assistant.sh:"
echo "1. Open functional_ai_assistant.sh in your editor"
echo "2. Find line 567: echo \"\$completion_date,\$completion_time,...\" >> \"learning_data/learning_log.csv\""
echo "3. Add the integration code above after that line"
echo "4. Make sure progress_updater.sh is executable: chmod +x progress_updater.sh"