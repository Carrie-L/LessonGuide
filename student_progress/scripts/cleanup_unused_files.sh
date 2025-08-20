#!/bin/bash

# 🧹 Cleanup Unused Files - Remove duplicate/unused tracking files

echo "🧹 Cleaning up unused progress tracking files..."

# Files that are no longer used (functional_ai_assistant.sh uses learning_log.csv only)
unused_files=(
    "learning_data/roadmap_progress.json"
    "learning_data/start_date.txt"
)

echo ""
echo "Files to remove (no longer used):"

for file in "${unused_files[@]}"; do
    if [[ -f "$file" ]]; then
        echo "  ❌ $file"
        rm "$file"
    else
        echo "  ✅ $file (already gone)"
    fi
done

echo ""
echo "🎯 Result: Now using learning_log.csv as single source of truth for all progress tracking!"
echo ""
echo "📊 Active tracking files:"
echo "  ✅ learning_data/learning_log.csv (all session data)"
echo "  ✅ learning_data/notes/ (learning notes)"
echo "  ✅ learning_data/daily_*.md (daily logs, if any)"

echo ""
echo "✅ Cleanup complete! System now uses simplified, single-file tracking."