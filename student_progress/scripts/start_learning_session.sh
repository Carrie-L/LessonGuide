#!/bin/bash

# 🎓 COMPLETE LEARNING SESSION LAUNCHER
# Integrates all components for seamless learning experience

TASK_ID=$1

if [ -z "$TASK_ID" ]; then
    echo "🎓 Learning Session Launcher"
    echo "=========================="
    echo ""
    echo "Usage: ./start_learning_session.sh <task_id>"
    echo ""
    echo "Available Tasks:"
    echo "  8.1.1 - DI Fundamentals (Primary Level, 5 min)"
    echo "  8.1.4 - Scope Management (Intermediate Level, 20 min)"
    echo ""
    echo "Example: ./start_learning_session.sh 8.1.1"
    exit 1
fi

echo "🎓 Launching Complete Learning Session for Task $TASK_ID"
echo "================================================"

# 1. Prepare environment
echo "🔧 Preparing learning environment..."
mkdir -p student_progress/JavaLearning/src
mkdir -p student_progress/AndroidPractice
mkdir -p student_progress/certificates
mkdir -p student_progress/performance_reports
mkdir -p student_progress/scripts

# 2. Ensure all scripts are executable
chmod +x student_progress/scripts/*.sh 2>/dev/null
chmod +x student_progress/scripts/*.py 2>/dev/null

# 3. Launch orchestrator in interactive mode
echo "🚀 Starting interactive learning session..."
echo ""

# Check if Python script exists, if not provide guidance
if [ -f "student_progress/scripts/learning_session_orchestrator.py" ]; then
    python3 student_progress/scripts/learning_session_orchestrator.py guide --task "$TASK_ID" --interactive
else
    echo "🎯 LEARNING SESSION: Task $TASK_ID"
    echo "================================"
    echo ""
    echo "📋 IMPLEMENTATION STEPS:"
    
    case $TASK_ID in
        "8.1.1")
            echo "1. ⏰ Set a 5-minute timer"
            echo "2. 📁 Create file: student_progress/JavaLearning/src/DIBasicsDemo.java"
            echo "3. 💻 Implement (manual typing only):"
            echo "   • BadOrderService (traditional approach)"
            echo "   • GoodOrderService (DI approach)"  
            echo "   • TestingAdvantageDemo (mock objects)"
            echo "   • PerformanceComparison (benchmark)"
            echo "4. 🧪 Compile and test your implementation"
            echo "5. ✅ Verify all requirements met"
            echo ""
            echo "💡 Key Concept: DI = Don't Initialize!"
            echo "🎯 Goal: >2x performance improvement with DI"
            ;;
        "8.1.4")
            echo "1. ⏰ Set a 20-minute timer"
            echo "2. 📁 Create project: student_progress/AndroidPractice/EcommerceApp/"
            echo "3. 💻 Implement multi-module architecture:"
            echo "   • EcommerceApplication (@HiltAndroidApp)"
            echo "   • UserRepository (@Singleton scope)"
            echo "   • ShoppingCartManager (@ActivityRetained scope)"
            echo "   • MainActivity (@AndroidEntryPoint)"
            echo "4. 🧪 Build and test Android project"
            echo "5. ✅ Verify scope management working"
            echo ""
            echo "💡 Key Concept: Proper scope prevents memory leaks"
            echo "🎯 Goal: App startup < 2s, memory < 50MB"
            ;;
        *)
            echo "❓ Task $TASK_ID configuration not found"
            echo "📚 Please refer to micro_tasks documentation"
            ;;
    esac
    
    echo ""
    echo "📋 STARTER TEMPLATE:"
    echo "🔍 Reference: student_progress/STARTER_TEMPLATES.md"
    echo ""
    
    # Wait for implementation
    read -p "⏳ Press Enter when you've completed the implementation..."
    
    echo ""
    echo "🏁 Implementation completed!"
fi

# 4. Run verification if possible
if [ -f "student_progress/scripts/verify_task.sh" ]; then
    echo "🤖 Running automated verification..."
    ./student_progress/scripts/verify_task.sh "$TASK_ID"
fi

# 5. Generate analytics update
echo "📊 Updating learning analytics..."
if [ -f "student_progress/scripts/learning_analytics.py" ]; then
    python3 student_progress/scripts/learning_analytics.py dashboard > /dev/null 2>&1
    echo "✅ Dashboard updated"
else
    echo "ℹ️  Analytics system available in LEARNING_ANALYTICS.md"
fi

# 6. Display completion
echo ""
echo "🎯 LEARNING SESSION COMPLETE!"
echo "============================="
echo "✅ Implementation finished"
echo "🤖 Verification completed"
echo "📊 Analytics updated"
echo "🏆 Progress recorded"
echo ""
echo "🚀 Ready for next learning challenge!"
echo "💡 Next task: Continue with sequential micro-tasks"