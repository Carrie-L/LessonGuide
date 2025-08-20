#!/bin/bash

# 🧹 Comprehensive Duplicate Content Cleanup
# Removes 31 duplicate/unused files to create single source of truth

echo "🧹 Starting comprehensive duplicate content cleanup..."
echo "📊 Removing 31 duplicate/unused files for cleaner system"
echo ""

removed_count=0

# Function to safely remove file
safe_remove() {
    local file="$1"
    if [[ -f "$file" ]]; then
        echo "  ❌ Removing: $file"
        rm "$file"
        ((removed_count++))
    else
        echo "  ✅ Already gone: $file"
    fi
}

echo "📁 Phase 1: Removing Framework Documentation Duplicates (14 files)"
echo "──────────────────────────────────────────────────────"

# Framework duplicates
safe_remove "micro_tasks/COMPREHENSIVE_HANDS_ON_FRAMEWORK_SUMMARY.md"
safe_remove "micro_tasks/HANDS_ON_CODING_FRAMEWORK.md"
safe_remove "micro_tasks/HANDS_ON_FRAMEWORK_TEMPLATE.md"
safe_remove "micro_tasks/FRAMEWORK_DEPLOYMENT_GUIDE.md"
safe_remove "micro_tasks/FRAMEWORK_IMPLEMENTATION_SUMMARY.md"
safe_remove "micro_tasks/MENTOR_TEACHING_GUIDE.md"
safe_remove "micro_tasks/INSTRUCTOR_MENTOR_GUIDE.md"
safe_remove "micro_tasks/PROGRESSIVE_PROJECT_ROADMAP.md"
safe_remove "micro_tasks/LEARNING_ANALYTICS_SYSTEM.md"
safe_remove "micro_tasks/LEARNING_EXPERIENCE_INTEGRATION.md"
safe_remove "micro_tasks/LEARNING_PATH_RECOMMENDATIONS.md"
safe_remove "micro_tasks/QUALITY_ASSURANCE_SYSTEM.md"
safe_remove "HANDS_ON_CODING_FRAMEWORK.md"
safe_remove "PROGRESSIVE_PROJECT_ROADMAP.md"

echo ""
echo "📁 Phase 2: Removing Enhanced/Alternative Versions (4 files)"
echo "──────────────────────────────────────────────────────"

# Enhanced versions (content moved to main files)
safe_remove "micro_tasks/ENHANCED_COLLECTIONS_FRAMEWORK.md"
safe_remove "micro_tasks/ENHANCED_MICRO_TASKS_C01_FOUNDATIONS.md"
safe_remove "micro_tasks/ENHANCED_MICRO_TASKS_C12_HTTP.md"
safe_remove "micro_tasks/KOTLIN_COROUTINES_MASTERY.md"

echo ""
echo "📁 Phase 3: Removing Student Progress Duplicates (6 files)"
echo "──────────────────────────────────────────────────────"

# Student progress duplicates (functionality integrated)
safe_remove "student_progress/LEARNING_ANALYTICS.md"
safe_remove "student_progress/LEARNING_EXPERIENCE_DEMO.md"
safe_remove "student_progress/LEARNING_SESSION_ORCHESTRATOR.md"
safe_remove "student_progress/SIMPLE_TRACKER_SETUP.md"
safe_remove "student_progress/SMART_TRACKER_GUIDE.md"
safe_remove "student_progress/STARTER_TEMPLATES.md"

echo ""
echo "📁 Phase 4: Removing Unused Progress Files (3 files)"
echo "──────────────────────────────────────────────────────"

# Unused progress tracking files
safe_remove "student_progress/learning_data/roadmap_progress.json"
safe_remove "student_progress/learning_data/start_date.txt"
safe_remove "student_progress/learning_data/completed_tasks.log"

echo ""
echo "📁 Phase 5: Removing Old Scripts (3 files)"
echo "──────────────────────────────────────────────────────"

# Old/replaced scripts
safe_remove "student_progress/ai_roadmap_assistant.sh"
safe_remove "student_progress/simple_learning_tracker.sh"
safe_remove "student_progress/smart_learning_tracker.sh"

echo ""
echo "🧹 Cleanup Complete!"
echo "══════════════════════════════════════════════════════"
echo "📊 Files removed: $removed_count"
echo ""
echo "✅ Essential files remaining:"
echo "   📖 ANDROID_SENIOR_DEVELOPER_ROADMAP.md (main roadmap)"
echo "   📖 CLAUDE.md (project instructions)"
echo "   📖 COMPREHENSIVE_HANDS_ON_FRAMEWORK_SUMMARY.md (framework overview)"
echo "   📖 MENTOR_TEACHING_GUIDE.md (teaching guide)"
echo "   📚 micro_tasks/MICRO_TASK_C01.md through C12.md (learning content)"
echo "   🤖 student_progress/functional_ai_assistant.sh (main system)"
echo "   🤖 student_progress/ai (shortcuts)"
echo "   📊 student_progress/learning_data/learning_log.csv (progress tracking)"
echo ""
echo "🎯 Benefits achieved:"
echo "   ✅ Single source of truth for all content"
echo "   ✅ No more confusion about which file to use"
echo "   ✅ Easier maintenance and updates"
echo "   ✅ Cleaner, more focused system"
echo ""
echo "Ready to use your streamlined Android Senior Developer learning system! 🚀"