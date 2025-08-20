#!/bin/bash

# 🤖 Functional AI Roadmap Assistant - Actually DOES things!
# Updated for correct file structure: MICRO_TASK_C01.md, MICRO_TASK_C02.md, etc.

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
CYAN='\033[0;36m'
PURPLE='\033[0;35m'
NC='\033[0m'

# File paths - UPDATED for correct structure
ROADMAP_FILE="../ANDROID_SENIOR_DEVELOPER_ROADMAP.md"
MICRO_TASKS_DIR="../micro_tasks"
PROGRESS_FILE="learning_data/roadmap_progress.json"

# Create directories
mkdir -p learning_data

# Initialize progress file
initialize_progress() {
    if [[ ! -f "$PROGRESS_FILE" ]]; then
        cat > "$PROGRESS_FILE" << 'EOF'
{
  "current_week": 1,
  "current_phase": "Phase 1: 基石篇",
  "current_step": "Step 1: 并发编程基础",
  "completed_tasks": [],
  "total_hours": 0,
  "start_date": "",
  "target_completion": "",
  "learning_pace": "normal"
}
EOF
    fi
}

# Show today's recommended tasks with ACTUAL file structure
show_todays_tasks() {
    clear
    echo -e "${BLUE}🎯 Today's Learning Focus - Functional AI Assistant${NC}"
    echo "═══════════════════════════════════════════════════════"
    
    # Read current progress
    current_week=1
    if [[ -f "$PROGRESS_FILE" ]]; then
        current_week=$(grep -o '"current_week": [0-9]*' "$PROGRESS_FILE" | grep -o '[0-9]*' | head -1)
    fi
    
    echo -e "${YELLOW}📅 Current Progress: Week $current_week/16${NC}"
    echo ""
    
    # Show week-specific recommendations with CORRECT file paths
    case $current_week in
        1)
            echo -e "${CYAN}📚 Week 1: Chapter 1 - 并发编程基础${NC}"
            echo "Today's Recommended Tasks:"
            echo ""
            echo -e "${GREEN}1.${NC} Task 1.1.1: synchronized互斥锁原理 (90min)"
            echo -e "   📁 File: ${YELLOW}micro_tasks/MICRO_TASK_C01.md${NC}"
            echo -e "   💻 Code: ${YELLOW}student_progress/c01/SynchronizedBasics.kt${NC}"
            echo -e "   🎯 Key: Understanding thread safety fundamentals"
            echo ""
            echo -e "${GREEN}2.${NC} Task 1.1.2: volatile内存可见性 (75min)"
            echo -e "   📁 File: ${YELLOW}micro_tasks/MICRO_TASK_C01.md${NC}"
            echo -e "   💻 Code: ${YELLOW}student_progress/c01/VolatileMemoryModel.kt${NC}"
            echo -e "   🎯 Key: Memory visibility between threads"
            ;;
        2)
            echo -e "${CYAN}📚 Week 2: Chapter 1 - 并发编程进阶${NC}"
            echo "Today's Recommended Tasks:"
            echo ""
            echo -e "${GREEN}1.${NC} Task 1.1.5: 锁升级机制观察 (105min)"
            echo -e "   📁 File: ${YELLOW}micro_tasks/MICRO_TASK_C01.md${NC}"
            echo ""
            echo -e "${GREEN}2.${NC} Task 1.1.6: CAS和ABA问题实践 (120min)"
            echo -e "   📁 File: ${YELLOW}micro_tasks/MICRO_TASK_C01.md${NC}"
            ;;
        3)
            echo -e "${CYAN}📚 Week 3: Chapter 2 - 集合框架基础${NC}"
            echo "Today's Recommended Tasks:"
            echo ""
            echo -e "${GREEN}1.${NC} Task 2.1.1: ArrayList动态数组实现 (90min)"
            echo -e "   📁 File: ${YELLOW}micro_tasks/MICRO_TASK_C02.md${NC}"
            echo -e "   💻 Code: ${YELLOW}student_progress/c02/CustomArrayList.kt${NC}"
            ;;
        *)
            echo -e "${YELLOW}⚠️ Week $current_week - Check roadmap for specific tasks${NC}"
            ;;
    esac
    
    echo ""
    echo -e "${PURPLE}💡 Available Commands:${NC}"
    echo "   ${CYAN}ai open-task [task_id]${NC}     → Actually open the file and show task"
    echo "   ${CYAN}ai setup-workspace [week]${NC}  → Create directory structure"  
    echo "   ${CYAN}ai start-task [task_id]${NC}    → Open file + create code file + start tracking"
}

# ACTUALLY open and show specific task content
open_task() {
    local task_id="$1"
    
    if [[ -z "$task_id" ]]; then
        echo -e "${RED}❌ Please specify task ID (e.g., 1.1.1, 2.1.1, 3.1.1)${NC}"
        return 1
    fi
    
    echo -e "${BLUE}🔍 Opening Task $task_id...${NC}"
    echo ""
    
    # Determine which file to open based on task ID
    local task_file=""
    local chapter=""
    
    case "$task_id" in
        1.1.*|1.2.*|1.3.*)
            task_file="$MICRO_TASKS_DIR/MICRO_TASK_C01.md"
            chapter="Chapter 1: 并发编程"
            ;;
        2.*)
            task_file="$MICRO_TASKS_DIR/MICRO_TASK_C02.md"
            chapter="Chapter 2: 集合框架"
            ;;
        3.*)
            task_file="$MICRO_TASKS_DIR/MICRO_TASK_C03.md"
            chapter="Chapter 3: 架构模式"
            ;;
        4.*)
            task_file="$MICRO_TASKS_DIR/MICRO_TASK_C04.md"
            chapter="Chapter 4: 性能优化"
            ;;
        5.*)
            task_file="$MICRO_TASKS_DIR/MICRO_TASK_C05.md"
            chapter="Chapter 5: 现代UI"
            ;;
        6.*)
            task_file="$MICRO_TASKS_DIR/MICRO_TASK_C06.md"
            chapter="Chapter 6: 测试框架"
            ;;
        12.*)
            task_file="$MICRO_TASKS_DIR/MICRO_TASK_C12.md"
            chapter="Chapter 12: HTTP协议"
            ;;
        *)
            echo -e "${RED}❌ Task $task_id not recognized${NC}"
            echo -e "${YELLOW}💡 Supported format: X.Y.Z (e.g., 1.1.1, 2.1.1, 3.1.1)${NC}"
            return 1
            ;;
    esac
    
    # Check if file exists
    if [[ ! -f "$task_file" ]]; then
        echo -e "${RED}❌ File not found: $task_file${NC}"
        return 1
    fi
    
    echo -e "${GREEN}📖 Found: $(basename "$task_file") - $chapter${NC}"
    echo -e "${YELLOW}📁 Full path: $task_file${NC}"
    echo ""
    
    # Find and show checkpoint levels instead of full content
    echo -e "${BLUE}🔍 Extracting learning checkpoints for Task $task_id...${NC}"
    
    # Extract checkpoint information
    local checkpoint_content=$(grep -A 30 "Task $task_id" "$task_file" | grep -E "(Primary Level|Intermediate Level|Senior Level|编程任务检查点)" | head -10)
    
    if [[ -n "$checkpoint_content" ]]; then
        echo -e "${GREEN}✅ Task found! Learning progression:${NC}"
        echo "═══════════════════════════════════════════════════════"
        echo "$checkpoint_content"
        echo "═══════════════════════════════════════════════════════"
        echo ""
        
        # Show next actions
        echo -e "${CYAN}🎯 Next Actions:${NC}"
        echo "   1. Review full task details in opened file"
        echo "   2. Follow Primary → Intermediate → Senior progression"
        echo "   3. Complete hands-on coding exercises"
        echo "   4. Use finish-task when done for assessment"
        
    else
        echo -e "${RED}❌ Task $task_id not found in $task_file${NC}"
        echo -e "${YELLOW}💡 Try browsing the file manually or check task ID format${NC}"
    fi
}

# ACTUALLY setup workspace with real directory creation
setup_workspace() {
    local week="$1"
    
    if [[ -z "$week" ]]; then
        echo -e "${RED}❌ Please specify week number (1-16)${NC}"
        return 1
    fi
    
    echo -e "${BLUE}🏗️ Setting up workspace for Week $week...${NC}"
    
    # Create actual directories based on week
    case "$week" in
        1|2)
            echo -e "${GREEN}📁 Creating: Week $week - Chapter 1 并发编程${NC}"
            
            # Create directories
            mkdir -p student_progress/c01/basics
            mkdir -p student_progress/c01/advanced  
            mkdir -p student_progress/c01/production
            
            # Create README with actual content
            cat > student_progress/c01/README.md << 'EOF'
# Chapter 1: 并发编程基础 (Concurrency Programming)

## Learning Goals
- Master Java/Kotlin concurrency fundamentals
- Understand thread safety and synchronization
- Implement lock-free programming techniques
- Build production-ready concurrent systems

## Directory Structure
- `basics/` - Fundamental synchronization concepts
- `advanced/` - Advanced concurrency patterns  
- `production/` - Enterprise-level implementations

## Progress Checklist
- [ ] Task 1.1.1: synchronized互斥锁原理
- [ ] Task 1.1.2: volatile内存可见性  
- [ ] Task 1.1.3: Thread.sleep线程休眠机制
- [ ] Task 1.1.4: volatile可见性验证
- [ ] Task 1.1.5: 锁升级机制观察
- [ ] Task 1.1.6: CAS和ABA问题实践
- [ ] Task 1.1.7: 生产级线程池实现
- [ ] Task 1.1.8: 企业级线程安全缓存系统

## Key Files
- Review: `../micro_tasks/MICRO_TASK_C01.md`
EOF
            echo -e "${GREEN}✅ Created: student_progress/c01/README.md${NC}"
            ;;
        3|4)
            echo -e "${GREEN}📁 Creating: Week $week - Chapter 2 集合框架${NC}"
            
            mkdir -p student_progress/c02/collections
            mkdir -p student_progress/c02/algorithms
            mkdir -p student_progress/c02/performance
            
            cat > student_progress/c02/README.md << 'EOF'
# Chapter 2: 集合框架深度 (Collections Framework)

## Learning Goals  
- Master Java collections internals
- Implement custom data structures
- Analyze performance characteristics
- Optimize collection usage patterns

## Progress Checklist
- [ ] Task 2.1.1: ArrayList动态数组实现
- [ ] Task 2.1.2: HashMap哈希表基础
- [ ] Task 2.1.3: LinkedList双向链表
- [ ] Task 2.1.4: TreeMap红黑树实现

## Key Files
- Review: `../micro_tasks/MICRO_TASK_C02.md`
EOF
            echo -e "${GREEN}✅ Created: student_progress/c02/README.md${NC}"
            ;;
        5|6)
            echo -e "${GREEN}📁 Creating: Week $week - Chapter 3 协程系统${NC}"
            
            mkdir -p student_progress/c03/coroutines
            mkdir -p student_progress/c03/flow
            mkdir -p student_progress/c03/android
            
            cat > student_progress/c03/README.md << 'EOF'
# Chapter 3: 协程机制掌握 (Coroutines Mastery)

## Learning Goals
- Master Kotlin coroutines fundamentals
- Implement asynchronous programming patterns
- Integrate coroutines with Android lifecycle
- Build reactive data flows

## Key Files  
- Review: `../micro_tasks/MICRO_TASK_C03.md` (if available)
EOF
            echo -e "${GREEN}✅ Created: student_progress/c03/README.md${NC}"
            ;;
        *)
            echo -e "${YELLOW}📁 Creating: Week $week workspace${NC}"
            mkdir -p "student_progress/week_$week"
            echo "# Week $week Learning" > "student_progress/week_$week/README.md"
            ;;
    esac
    
    echo ""
    echo -e "${GREEN}✅ Workspace ready for Week $week!${NC}"
    echo -e "${CYAN}📂 Directory: student_progress/${NC}"
    
    # Show what was created
    if [[ -d "student_progress" ]]; then
        echo -e "${YELLOW}📋 Created structure:${NC}"
        find student_progress -type d | head -10 | while read dir; do
            echo "   📁 $dir"
        done
    fi
}

# ACTUALLY start a task - opens file, creates code file, starts tracking, opens IDEs
start_task() {
    local task_id="$1"
    
    if [[ -z "$task_id" ]]; then
        echo -e "${RED}❌ Please specify task ID (e.g., 1.1.1)${NC}"
        return 1
    fi
    
    echo -e "${BLUE}🚀 Starting Task $task_id - Enhanced Full Setup${NC}"
    echo "═══════════════════════════════════════════════════════"
    
    # Step 1: Open and show task content
    open_task "$task_id"
    
    if [[ $? -ne 0 ]]; then
        return 1
    fi
    
    echo ""
    echo -e "${BLUE}🏗️ Step 2: Creating code workspace...${NC}"
    
    # Step 2: Create appropriate code file
    local code_file=""
    local chapter_dir=""
    
    case "$task_id" in
        1.1.1)
            chapter_dir="c01"
            code_file="student_progress/c01/SynchronizedBasics.kt"
            ;;
        1.1.2) 
            chapter_dir="c01"
            code_file="student_progress/c01/VolatileMemoryModel.kt"
            ;;
        1.1.3)
            chapter_dir="c01"
            code_file="student_progress/c01/ThreadSleepDemo.kt"
            ;;
        2.1.1)
            chapter_dir="c02" 
            code_file="student_progress/c02/CustomArrayList.kt"
            ;;
        *)
            chapter_dir="misc"
            code_file="student_progress/misc/Task_${task_id//\./_}.kt"
            ;;
    esac
    
    # Create directory structure
    mkdir -p "$(dirname "$code_file")"
    
    # Create code file with template if it doesn't exist
    if [[ ! -f "$code_file" ]]; then
        cat > "$code_file" << EOF
/**
 * Task $task_id Implementation
 * 
 * TODO: Implement the solution for Task $task_id
 * 
 * Key Requirements:
 * - [Add specific requirements from the task]
 * 
 * Learning Goals:
 * - [Add learning objectives]
 */

fun main() {
    println("Starting Task $task_id implementation")
    
    // TODO: Add your implementation here
    
    println("Task $task_id completed")
}

// TODO: Add your classes and functions here
EOF
        echo -e "${GREEN}✅ Created code file: $code_file${NC}"
    else
        echo -e "${YELLOW}📝 Code file already exists: $code_file${NC}"
    fi
    
    # Step 3: Start tracking
    echo ""
    echo -e "${BLUE}📊 Step 3: Starting progress tracking...${NC}"
    
    # Start session tracking
    echo "$(date '+%s')" > learning_data/session_start.tmp
    echo "Task $task_id" > learning_data/current_task.tmp
    echo -e "${GREEN}✅ Session tracking started${NC}"
    
    # Step 4: Prepare file paths and positioning
    echo ""
    echo -e "${BLUE}🗂️ Step 4: Preparing file paths and task positioning...${NC}"
    
    # Determine task file and get line number for task positioning
    local task_file=""
    local line_number=""
    
    case "$task_id" in
        1.1.*|1.2.*|1.3.*)
            task_file="$MICRO_TASKS_DIR/MICRO_TASK_C01.md"
            # Find line number of the task
            line_number=$(grep -n "Task $task_id" "$task_file" | head -1 | cut -d: -f1)
            ;;
        2.*)
            task_file="$MICRO_TASKS_DIR/MICRO_TASK_C02.md"
            line_number=$(grep -n "Task $task_id" "$task_file" | head -1 | cut -d: -f1)
            ;;
        3.*)
            task_file="$MICRO_TASKS_DIR/MICRO_TASK_C03.md"
            line_number=$(grep -n "Task $task_id" "$task_file" | head -1 | cut -d: -f1)
            ;;
    esac
    
    echo -e "${GREEN}✅ File paths prepared:${NC}"
    echo "   📖 Task File: $task_file (Line: $line_number)"
    echo "   💻 Code File: $code_file"
    echo "   📊 Tracking: Active session started"
    echo ""
    
    # Step 5: Smart IDE Detection and Opening
    echo -e "${BLUE}🚀 Step 5: Opening IDEs automatically...${NC}"
    
    # Open task file with positioning (VS Code or default text editor)
    if command -v code &> /dev/null && [[ -n "$task_file" && -n "$line_number" ]]; then
        echo -e "${GREEN}📖 Opening task file in VS Code at line $line_number...${NC}"
        code --goto "$task_file:$line_number"
    elif [[ -n "$task_file" ]]; then
        echo -e "${YELLOW}📖 Task file: $task_file${NC}"
    fi
    
    # Smart IDE detection for code files
    echo -e "${GREEN}💻 Opening code file in appropriate IDE...${NC}"
    
    if [[ "$code_file" == *.kt ]]; then
        # Kotlin files -> Android Studio
        if [[ -f "/mnt/t/Android/AndroidStudio/bin/studio64.exe" ]]; then
            echo -e "${GREEN}🤖 Opening Kotlin file in Android Studio...${NC}"
            "/mnt/t/Android/AndroidStudio/bin/studio64.exe" "$code_file" &
        elif command -v code &> /dev/null; then
            echo -e "${YELLOW}📝 Android Studio not found, opening in VS Code...${NC}"
            code "$code_file"
        fi
    elif [[ "$code_file" == *.java ]]; then
        # Java files -> IntelliJ IDEA
        if [[ -f "/mnt/c/Program Files/JetBrains/IntelliJ IDEA Community Edition 2024.3.4.1/bin/idea64.exe" ]]; then
            echo -e "${GREEN}☕ Opening Java file in IntelliJ IDEA...${NC}"
            "/mnt/c/Program Files/JetBrains/IntelliJ IDEA Community Edition 2024.3.4.1/bin/idea64.exe" "$code_file" &
        elif command -v code &> /dev/null; then
            echo -e "${YELLOW}📝 IntelliJ IDEA not found, opening in VS Code...${NC}"
            code "$code_file"
        fi
    elif [[ -d "$(dirname "$code_file")/src" ]] || [[ -f "$(dirname "$code_file")/../build.gradle" ]]; then
        # Android project structure detected -> Android Studio
        if [[ -f "/mnt/t/Android/AndroidStudio/bin/studio64.exe" ]]; then
            echo -e "${GREEN}🤖 Android project detected, opening in Android Studio...${NC}"
            "/mnt/t/Android/AndroidStudio/bin/studio64.exe" "$(dirname "$code_file")/.." &
        fi
    else
        # Fallback to VS Code
        if command -v code &> /dev/null; then
            echo -e "${GREEN}📝 Opening in VS Code...${NC}"
            code "$code_file"
        fi
    fi
    
    # Step 6: Clear finish command instructions
    echo ""
    echo -e "${PURPLE}🎯 When you finish coding:${NC}"
    echo -e "${CYAN}   ./functional_ai_assistant.sh finish-task${NC}  ← Use this command ONLY"
    echo ""
    echo -e "${GREEN}✅ Setup complete! Happy coding! 🚀${NC}"
}

# Finish task and review
finish_task() {
    if [[ ! -f "learning_data/current_task.tmp" ]]; then
        echo -e "${RED}❌ No active task found${NC}"
        return 1
    fi
    
    local task_name=$(cat learning_data/current_task.tmp)
    
    echo -e "${BLUE}🎯 Task Completion Review: $task_name${NC}"
    echo "═══════════════════════════════════════════════════════"
    
    # Calculate session duration
    local duration_minutes=0
    local start_time=""
    local current_time=$(date '+%s')
    local completion_date=$(date '+%Y-%m-%d')
    local completion_time=$(date '+%H:%M')
    
    if [[ -f "learning_data/session_start.tmp" ]]; then
        start_time=$(cat learning_data/session_start.tmp)
        duration_minutes=$(( (current_time - start_time) / 60 ))
        
        echo -e "${GREEN}✅ Session completed successfully!${NC}"
        echo "   ⏰ Duration: $duration_minutes minutes"
        echo "   📚 Task: $task_name"
        echo "   📅 Completed: $completion_date $completion_time"
    else
        echo -e "${YELLOW}⚠️ No session timing data found${NC}"
        echo -e "${GREEN}✅ Task marked as completed: $task_name${NC}"
        duration_minutes=0
    fi
    
    echo ""
    echo -e "${BLUE}📝 Learning Assessment (helps track your progress):${NC}"
    echo ""
    
    # Initialize learning_log.csv if it doesn't exist or fix header if needed
    if [[ ! -f "learning_data/learning_log.csv" ]]; then
        echo "Date,Time,Task,Duration_Minutes,Quality_Self_Rating,Difficulty_Rating,Completion_Status,Notes,Task_File,Task_ID" > "learning_data/learning_log.csv"
    else
        # Check if header needs updating (fix mismatch issue)
        local current_header=$(head -1 "learning_data/learning_log.csv")
        local expected_header="Date,Time,Task,Duration_Minutes,Quality_Self_Rating,Difficulty_Rating,Completion_Status,Notes,Task_File,Task_ID"
        
        if [[ "$current_header" != "$expected_header" ]]; then
            echo -e "${YELLOW}⚠️ Updating CSV header to match data format...${NC}"
            # Backup existing data (skip header)
            tail -n +2 "learning_data/learning_log.csv" > "learning_data/learning_log_temp.csv"
            # Write correct header
            echo "$expected_header" > "learning_data/learning_log.csv"
            # Append existing data
            cat "learning_data/learning_log_temp.csv" >> "learning_data/learning_log.csv"
            # Clean up
            rm -f "learning_data/learning_log_temp.csv"
            echo -e "${GREEN}✅ CSV header updated successfully${NC}"
        fi
    fi
    
    # Get user feedback (questionnaire with numeric inputs)
    read -p "🎯 Quality of your work (1-10): " quality_rating
    read -p "🧠 Task difficulty for you (1-10): " difficulty_rating
    
    echo "✅ Completion status:"
    echo "   1 = Completed"
    echo "   2 = Partial" 
    echo "   3 = Struggling"
    read -p "Choose (1/2/3): " completion_choice
    
    # Convert numeric input to text for CSV
    case "$completion_choice" in
        1) completion_status="completed" ;;
        2) completion_status="partial" ;;
        3) completion_status="struggling" ;;
        *) completion_status="partial" ;;  # Default fallback
    esac
    
    read -p "💭 What you learned or struggled with: " notes
    
    # Extract task ID from task name
    local task_id=$(echo "$task_name" | grep -o "Task [0-9]\+\.[0-9]\+\.[0-9]\+" | head -1)
    if [[ -z "$task_id" ]]; then
        task_id="Unknown"
    fi
    
    # Determine task file
    local task_file=""
    case "$task_id" in
        *"1.1."*|*"1.2."*|*"1.3."*)
            task_file="MICRO_TASK_C01.md"
            ;;
        *"2."*)
            task_file="MICRO_TASK_C02.md"
            ;;
        *)
            task_file="Unknown"
            ;;
    esac
    
    # Save to learning_log.csv (consolidated tracking)
    echo "$completion_date,$completion_time,$task_name,$duration_minutes,$quality_rating,$difficulty_rating,$completion_status,$notes,$task_file,$task_id" >> "learning_data/learning_log.csv"
    
    # Create learning notes file
    local notes_dir="learning_data/notes"
    mkdir -p "$notes_dir"
    local note_file="$notes_dir/${task_id//[^a-zA-Z0-9]/_}_notes.md"
    
    echo ""
    echo -e "${BLUE}📝 Creating learning notes file...${NC}"
    
    # Create detailed notes file
    cat > "$note_file" << EOF
# Learning Notes: $task_name

**Date**: $completion_date $completion_time  
**Duration**: $duration_minutes minutes  
**Quality Rating**: $quality_rating/10  
**Difficulty Rating**: $difficulty_rating/10  
**Status**: $completion_status

## 🎯 Key Learnings
$notes

## 💻 Code Implementation
- **File**: $code_file
- **Task File**: $task_file

## 📊 Reflection Questions
1. **What was the most challenging part?**
   - 

2. **What would you do differently next time?**
   - 

3. **How does this connect to previous learning?**
   - 

4. **What questions do you still have?**
   - 

## 🔗 Related Concepts
- [ ] 
- [ ] 
- [ ] 

## 📚 Additional Resources
- [ ] 
- [ ] 

---
*Generated by functional_ai_assistant.sh*
EOF

    echo -e "${GREEN}✅ Notes file created: $note_file${NC}"
    
    # Open notes file in VS Code if available
    if command -v code &> /dev/null; then
        echo -e "${BLUE}📖 Opening notes file in VS Code...${NC}"
        code "$note_file"
    fi
    
    # Clean up session files
    rm -f learning_data/session_start.tmp learning_data/current_task.tmp
    
    # Show next steps with roadmap positioning
    echo ""
    echo -e "${PURPLE}🎯 Next Steps:${NC}"
    echo "   1. Review your code implementation"
    echo "   2. Test thoroughly"
    echo "   3. Complete reflection in opened notes file"
    echo "   4. Move to next task in roadmap"
    
    # Open roadmap and find next task
    local roadmap_file="../ANDROID_SENIOR_DEVELOPER_ROADMAP.md"
    if [[ -f "$roadmap_file" ]]; then
        echo ""
        echo -e "${BLUE}📋 Opening roadmap with your current position...${NC}"
        
        # Find next task based on current task
        local next_task=""
        case "$task_id" in
            *"1.1.1"*) next_task="1.1.2" ;;
            *"1.1.2"*) next_task="1.1.3" ;;
            *"1.1.3"*) next_task="1.1.4" ;;
            *"1.1.4"*) next_task="1.1.5" ;;
            *"1.1.5"*) next_task="1.1.6" ;;
            *"1.2.1"*) next_task="1.2.2" ;;
            *"1.3.1"*) next_task="1.3.2" ;;
            *) next_task="Check roadmap for next task" ;;
        esac
        
        if [[ "$next_task" != "Check roadmap for next task" ]]; then
            echo -e "${GREEN}🎯 Your next task: Task $next_task${NC}"
            echo -e "${CYAN}Quick start: ./functional_ai_assistant.sh start-task $next_task${NC}"
        fi
        
        # Open roadmap in VS Code if available
        if command -v code &> /dev/null; then
            echo -e "${BLUE}📖 Opening roadmap in VS Code...${NC}"
            code "$roadmap_file"
        fi
    fi
    
    # Show learning report after completion (from simple_learning_tracker.sh)
    echo ""
    echo -e "${BLUE}📊 Quick Learning Analytics${NC}"
    generate_quick_report
}

# Generate quick learning report (adapted from simple_learning_tracker.sh)
generate_quick_report() {
    local log_file="learning_data/learning_log.csv"
    
    if [[ ! -f "$log_file" ]]; then
        echo -e "${YELLOW}⚠️ No learning data found yet${NC}"
        return
    fi
    
    # Basic statistics
    local total_sessions=$(tail -n +2 "$log_file" | wc -l)
    local total_minutes=$(tail -n +2 "$log_file" | cut -d',' -f4 | awk '{sum+=$1} END {print sum+0}')
    
    # This week's data
    local current_week=$(date '+%Y-%m-%d' -d 'monday')
    local this_week_sessions=$(tail -n +2 "$log_file" | awk -F',' -v week="$current_week" '$1 >= week' | wc -l)
    
    echo "═══════════════════════════════════════════════════════"
    echo -e "${YELLOW}🎯 Your Progress Summary:${NC}"
    echo "   📈 Total Sessions: $total_sessions"
    echo "   ⏰ Total Time: $total_minutes minutes ($(($total_minutes / 60))h $(($total_minutes % 60))m)"
    echo "   📅 This Week: $this_week_sessions sessions"
    
    # Show recent sessions
    if [[ $total_sessions -gt 0 ]]; then
        echo ""
        echo -e "${YELLOW}📊 Recent Learning:${NC}"
        tail -n 3 "$log_file" | while IFS=',' read -r date time task duration quality difficulty status notes task_file task_id; do
            if [[ "$date" != "Date" && -n "$date" ]]; then
                echo "   • $date: $task (${duration}min, Quality: ${quality}/10)"
            fi
        done
    fi
    
    # Quick recommendations
    if [[ $this_week_sessions -lt 3 && $total_sessions -gt 3 ]]; then
        echo ""
        echo -e "${BLUE}💡 Tip: Try to maintain at least 3 learning sessions per week for best progress!${NC}"
    fi
    
    echo "═══════════════════════════════════════════════════════"
}

# Show actual file browser
browse_files() {
    echo -e "${BLUE}📁 Micro Tasks File Browser${NC}"
    echo "═══════════════════════════════════════════════════════"
    
    local counter=1
    for file in "$MICRO_TASKS_DIR"/MICRO_TASK_C*.md; do
        if [[ -f "$file" ]]; then
            filename=$(basename "$file")
            case "$filename" in
                "MICRO_TASK_C01.md")
                    echo -e "${GREEN}$counter.${NC} ${CYAN}$filename${NC} - Chapter 1: 并发编程基础"
                    ;;
                "MICRO_TASK_C02.md")
                    echo -e "${GREEN}$counter.${NC} ${CYAN}$filename${NC} - Chapter 2: 集合框架深度"
                    ;;
                "MICRO_TASK_C03.md")
                    echo -e "${GREEN}$counter.${NC} ${CYAN}$filename${NC} - Chapter 3: 架构模式"
                    ;;
                "MICRO_TASK_C06.md")
                    echo -e "${GREEN}$counter.${NC} ${CYAN}$filename${NC} - Chapter 6: 测试框架"
                    ;;
                "MICRO_TASK_C12.md")
                    echo -e "${GREEN}$counter.${NC} ${CYAN}$filename${NC} - Chapter 12: HTTP协议"
                    ;;
                *)
                    echo -e "${GREEN}$counter.${NC} ${CYAN}$filename${NC}"
                    ;;
            esac
            ((counter++))
        fi
    done
    
    echo ""
    read -p "📖 Enter file number to preview, or press Enter to go back: " choice
    
    if [[ -n "$choice" ]]; then
        # Show file preview
        local files=("$MICRO_TASKS_DIR"/MICRO_TASK_C*.md)
        if [[ "$choice" -ge 1 && "$choice" -le ${#files[@]} ]]; then
            local selected_file="${files[$((choice-1))]}"
            echo ""
            echo -e "${BLUE}📖 Preview: $(basename "$selected_file")${NC}"
            echo "═══════════════════════════════════════════════════════"
            
            # Show first few tasks from the file
            head -50 "$selected_file" | grep -A 3 "Task [0-9]"
            
            echo ""
            echo -e "${CYAN}💡 Full file path: $selected_file${NC}"
            echo -e "${CYAN}💡 Open with: code \"$selected_file\"${NC}"
        fi
    fi
}

# Main command dispatcher
case "${1:-help}" in
    "today"|"roadmap-today")
        initialize_progress
        show_todays_tasks
        ;;
    "open-task")
        open_task "$2"
        ;;
    "setup-workspace")  
        setup_workspace "$2"
        ;;
    "start-task")
        start_task "$2"
        ;;
    "finish-task")
        finish_task
        ;;
    "browse"|"browse-files")
        browse_files
        ;;
    "help"|"--help"|"-h")
        echo -e "${BLUE}🤖 Functional AI Roadmap Assistant${NC}"
        echo "═══════════════════════════════════════════════════════"
        echo ""
        echo -e "${YELLOW}🚀 Main Commands:${NC}"
        echo "  ./functional_ai_assistant.sh today              → Show today's recommended tasks"
        echo "  ./functional_ai_assistant.sh open-task 1.1.1    → Actually open and show task content"
        echo "  ./functional_ai_assistant.sh start-task 1.1.1   → Full setup: open file + create code + track"
        echo "  ./functional_ai_assistant.sh finish-task        → Complete current task"
        echo ""
        echo -e "${YELLOW}🛠️ Utility Commands:${NC}"
        echo "  ./functional_ai_assistant.sh setup-workspace 1  → Create week directory structure"
        echo "  ./functional_ai_assistant.sh browse             → Browse available micro task files"
        echo ""
        echo -e "${CYAN}🎯 Example Workflow:${NC}"
        echo "  1. ./functional_ai_assistant.sh today           → See today's tasks"
        echo "  2. ./functional_ai_assistant.sh start-task 1.1.1 → Setup everything + open IDEs"
        echo "  3. [Code for 5-90 minutes in Android Studio/IntelliJ/VS Code]"
        echo "  4. ./functional_ai_assistant.sh finish-task     → Complete and review (ONLY command to use)"
        echo ""
        echo -e "${PURPLE}💡 Updated for correct file structure:${NC}"
        echo "  ✅ MICRO_TASK_C01.md (Chapter 1)"
        echo "  ✅ MICRO_TASK_C02.md (Chapter 2)"  
        echo "  ✅ MICRO_TASK_C03.md (Chapter 3)"
        echo "  ✅ etc."
        ;;
    *)
        echo -e "${RED}❌ Unknown command: $1${NC}"
        echo -e "${YELLOW}💡 Run './functional_ai_assistant.sh help' for available commands${NC}"
        ;;
esac