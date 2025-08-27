#!/bin/bash

# 🤖 Functional AI Roadmap Assistant - Actually DOES things!
# Updated for correct file structure: MICRO_TASK_C01.md, MICRO_TASK_C02.md, etc.
#
# 🧠 INTEGRATED LEARNING ANALYTICS SYSTEM:
# - Automatically runs AI performance analysis on task completion
# - Generates detailed assessment reports with scoring
# - Updates performance database for tracking progress
# - Integrates with Claude's memory system for personalized learning
# - Uses analytics/learning_performance_tracker.py for AI-powered analysis

# `functional_ai_assistant.sh` 是一支“学习流程自动化管家”脚本，围绕你的 Android 面试微任务体系，提供了以下核心能力：

# 1. 基础配置  
#    • 颜色常量，文件路径常量（Roadmap、Micro-tasks、进度 JSON、日志 CSV 等）  
#    • 自动创建 `learning_data/` 目录与 `roadmap_progress.json`（`initialize_progress`）

# 2. 今日学习概览 – `show_todays_tasks`  
#    • 读取 `roadmap_progress.json` 中的 `current_week`  
#    • 根据周次打印当天推荐任务、对应 MD 文件与代码文件  
#    • 列出可用的快捷命令

# 3. 打开任务 – `open_task <task_id>`  
#    • 根据任务号（如 1.1.5）定位对应的 `MICRO_TASK_Cxx.md`  
#    • 打印任务所在章节、文件、行号，并预览 10 行上下文  
#    • 给出后续行动提示

# 4. 一键搭建周工作区 – `setup_workspace <week>`  
#    • 按周次创建 `student_progress/c01、c02、c03 …` 等目录和 README  
#    • 方便分类存放代码 / 笔记

# 5. 全流程启动任务 – `start_task <task_id>`  
#    Step-by-step 自动化：  
#    a. 调用 `open_task` 预览任务  
#    b. 生成代码文件（.kt / .java）及其目录，如果不存在则写入模板头；遵循“禁止复制粘贴”，留空 TODO  
#    c. 创建会话跟踪文件 `session_start.tmp`、`current_task.tmp`  
#    d. 根据文件类型 & 开发环境智能打开 IDE（Android Studio / IntelliJ / VS Code）  
#    e. 提示结束后使用 `./ai f` 唯一命令完成任务

# 6. 结束任务与评估 – `finish_task`  
#    • 计算本次会话耗时（分钟）  
#    • 交互式收集自评分（质量、难度、完成度、笔记）  
#    • 追加一行到 `learning_data/learning_log.csv`（自动修复表头不一致）  
#    • 生成学习笔记文件 `learning_data/notes/<task_id>_notes.md` 并自动打开  
#    • 调用 `analytics/learning_performance_tracker.py` 做 AI 绩效分析，生成评估报告  
#    • 输出下一个建议任务，打开路线图  
#    • 展示快速统计 `generate_quick_report` 与学习日历 `show_calendar`  
#    • 清理临时会话文件

# 7. 数据分析与报告  
#    • `generate_quick_report`：读取 CSV 统计总时长、平均质量、本周次数、挑战任务等  
#    • `generate_report`：更详细的进度、平均分、Streak、洞察与建议  
#    • `show_calendar`：过去 14 天学习打卡可视化

# 8. 文件浏览器 – `browse_files`  
#    • 罗列 `micro_tasks` 目录内所有 `MICRO_TASK_Cxx.md`，编号显示章节  
#    • 支持输入数字预览文件前 50 行

# 9. 命令调度器  
#    • `./ai today`           → 今日任务  
#    • `./ai o 1.1.1`        → 打开任务  
#    • `./ai s 1.1.1`        → 全流程启动任务  
#    • `./ai f`              → 完成任务并评估  
#    • `./ai w 1`            → 建立周工作区  
#    • `./ai b`              → 浏览文件  
#    • `./ai help`           → 帮助

# 10. 整体价值  
#     • 把“读任务 → 创建代码 → 计时 → 评估 → 统计”流水线高度自动化  
#     • 强化 No-copy-paste、定时 Session、AI 质量分析、学习数据可视化  
#     • 让 5-分钟微任务的闭环操作只需几条命令完成，提高专注与反馈速度

# 换言之，这个脚本就是你的“自助学习平台 + 进度打卡 + AI 教练”整合工具。

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
# PROGRESS_FILE="learning_data/roadmap_progress.json"
PROGRESS_FILE="../PROGRESS.md"
TIMELINE_FILE="../TIMELINE.md"
progress=$(grep -m1 -n "\- \[.\] Task" "$PROGRESS_FILE" | head -1)
# 例如得到 “12|- [ ] Task 1.1.5”
today_line=$(grep -n "$(date +%m/%d | sed 's/^0//')" "$TIMELINE_FILE" | head -1)

# Get current date and time
DATE=$(date '+%Y-%m-%d')
TIME=$(date '+%H:%M')
TIMESTAMP=$(date '+%s')

# Learning data files
LOG_FILE="learning_data/learning_log.csv"
DAILY_LOG="learning_data/daily_${DATE}.md"

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
            echo -e "   💻 Code: ${YELLOW}student_code/c01/SynchronizedBasics.kt${NC}"
            echo -e "   🎯 Key: Understanding thread safety fundamentals"
            echo ""
            echo -e "${GREEN}2.${NC} Task 1.1.2: volatile内存可见性 (75min)"
            echo -e "   📁 File: ${YELLOW}micro_tasks/MICRO_TASK_C01.md${NC}"
            echo -e "   💻 Code: ${YELLOW}student_code/c01/VolatileMemoryModel.kt${NC}"
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
            echo -e "   💻 Code: ${YELLOW}student_code/c02/CustomArrayList.kt${NC}"
            ;;
        *)
            echo -e "${YELLOW}⚠️ Week $current_week - Check roadmap for specific tasks${NC}"
            ;;
    esac
    
    echo ""
    echo -e "${PURPLE}💡 Available Commands:${NC}"
    echo "   ./ai t           # Show today's tasks  "
    echo "   ./ai s 1.1.1     # Start task 1.1.1"
    echo "   ./ai f           # Finish current task"
    echo "   ./ai o 1.1.1     # Open task 1.1.1"
    echo "   ./ai w 1         # Setup workspace week 1"
    echo "   ./ai b           # Browse files"
}

# ACTUALLY open and show specific task content
open_task() {
    local task_id="$1"
    
    if [[ -z "$task_id" ]]; then
        echo -e "${RED}❌ Please specify task ID (e.g., 1.1.1, 2.1.1, 3.1.1)${NC}"
        return 1
    fi
    
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
    local line_number=$(grep -n "Task $task_id" "$task_file" | head -1 | cut -d: -f1)
    echo "📖 Task File: $task_file (Line: $line_number)"
    echo ""
    
    # Extract checkpoint information
    local checkpoint_content=$(grep -A 30 "Task $task_id" "$task_file"  | head -10)
    
    if [[ -n "$checkpoint_content" ]]; then
        # Show next actions
        echo -e "${CYAN}🎯 下一步行动:${NC}"
        echo "   1. 阅读打开文件中的完整任务细节"
        echo "   2. 按照初级 → 中级 → 高级的顺序进行学习"
        echo "   3. 完成实际的编码练习"
        echo "   4. 使用 finish-task 完成任务后进行评估"
        echo "   5. 完成小结笔记，用自己的语言总结输出"
        
    else
        echo -e "${RED}❌ Task $task_id not found in $task_file${NC}"
        echo -e "${YELLOW}💡 尝试手动浏览文件或检查任务ID格式${NC}"
    fi

    echo "═══════════════════════════════════════════════════════"
}

# ACTUALLY setup workspace with real directory creation
setup_workspace() {
    local week="$1"
    
    if [[ -z "$week" ]]; then
        echo -e "${RED}❌ Please specify week number (1-16)${NC}"
        return 1
    fi
    
    echo -e "${BLUE}🏗️ Setting up workspace for Week $week...${NC}"
    
    # 根据新的时间线映射 Week→Chapter
    case "$week" in
        1)
            echo -e "${GREEN}📁 Week 1 → Chapters 1,2,3,9${NC}"
            chapters=("c01" "c02" "c03" "c09")
            ;;
        2)
            echo -e "${GREEN}📁 Week 2 → Chapter 4${NC}"
            chapters=("c04")
            ;;
        3)
            echo -e "${GREEN}📁 Week 3 → Chapters 5,8${NC}"
            chapters=("c05" "c08")
            ;;
        4)
            echo -e "${GREEN}📁 Week 4 → Chapter 10${NC}"
            chapters=("c10")
            ;;
        5)
            echo -e "${GREEN}📁 Week 5 → Chapters 11,12${NC}"
            chapters=("c11" "c12")
            ;;
        6)
            echo -e "${GREEN}📁 Week 6 → Chapters 6,7${NC}"
            chapters=("c06" "c07")
            ;;
        *)
            echo -e "${YELLOW}📁 Week $week - custom workspace${NC}"
            chapters=("week_$week")
            ;;
    esac

    # 批量创建目录与 README
    for chapter in "${chapters[@]}"; do
        mkdir -p "student_code/${chapter}"
        readme="student_code/${chapter}/README.md"
        if [[ ! -f "$readme" ]]; then
            echo "# Workspace for ${chapter^^}" > "$readme"
            echo "- Auto-generated by setup_workspace" >> "$readme"
        fi
        echo -e "${GREEN}✅ Ready: ${chapter}${NC}"
    done
    
    echo ""
    echo -e "${GREEN}✅ Workspace ready for Week $week!${NC}"
    echo -e "${CYAN}📂 Directory: student_code/${NC}"
    
    # Show what was created
    if [[ -d "student_code" ]]; then
        echo -e "${YELLOW}📋 Created structure:${NC}"
        find student_code -type d | head -10 | while read dir; do
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
        1.1)
            chapter_dir="c01"
            code_file="student_code/c01/"
            ;;
        1.2) 
            chapter_dir="c01"
            code_file="student_code/c01/VolatileMemoryModel.kt"
            ;;
        1.1.3)
            chapter_dir="c01"
            code_file="student_code/c01/ThreadSleepDemo.kt"
            ;;
        2.1.1)
            chapter_dir="c02" 
            code_file="student_code/c02/CustomArrayList.kt"
            ;;
        *)
            chapter_dir="misc"
            code_file="student_code/misc/Task_${task_id//\./_}.kt"
            ;;
    esac
    
    # Create directory structure
    # mkdir -p "$(dirname "$code_file")"
    
    # Create code file with template if it doesn't exist
#     if [[ ! -f "$code_file" ]]; then
#         cat > "$code_file" << EOF
# /**
#  * Task $task_id Implementation
#  * 
#  * TODO: Implement the solution for Task $task_id
#  * 
#  * Key Requirements:
#  * - [Add specific requirements from the task]
#  * 
#  * Learning Goals:
#  * - [Add learning objectives]
#  */

# fun main() {
#     println("Starting Task $task_id implementation")
    
#     // TODO: Add your implementation here
    
#     println("Task $task_id completed")
# }

# // TODO: Add your classes and functions here
# EOF
#         echo -e "${GREEN}✅ Created code file: $code_file${NC}"
#     else
#         echo -e "${YELLOW}📝 Code file already exists: $code_file${NC}"
#     fi
    
    # Step 3: Start tracking
    echo ""
    echo -e "${BLUE}📊 Step 3: Starting progress tracking...${NC}"
    
    # Start session tracking
    echo "$(date '+%s')" > learning_data/session_start.tmp
    echo "Task $task_id" > learning_data/current_task.tmp
    echo -e "${GREEN}✅ Session tracking started${NC}"
    echo ""
   
    
    # Step 4: Smart IDE Detection and Opening
    # echo -e "${BLUE}🚀 Step 4: Opening IDEs automatically...${NC}"
    
    # Open task file with positioning (VS Code or default text editor)
    # if command -v code &> /dev/null && [[ -n "$task_file" && -n "$line_number" ]]; then
    #     echo -e "${GREEN}📖 Opening task file in VS Code at line $line_number...${NC}"
    #     code --goto "$task_file:$line_number"
    # elif [[ -n "$task_file" ]]; then
    #     echo -e "${YELLOW}📖 Task file: $task_file${NC}"
    # fi
    
    # Smart IDE detection for code files
    # echo -e "${GREEN}💻 Opening code file in appropriate IDE...${NC}"
    
    # if [[ "$code_file" == *.kt ]]; then
    #     Kotlin files -> Android Studio
    #     if [[ -f "/mnt/t/Android/AndroidStudio/bin/studio64.exe" ]]; then
    #         echo -e "${GREEN}🤖 Opening Kotlin file in Android Studio...${NC}"
    #         "/mnt/t/Android/AndroidStudio/bin/studio64.exe" "$code_file" &
    #     elif command -v code &> /dev/null; then
    #         echo -e "${YELLOW}📝 Android Studio not found, opening in VS Code...${NC}"
    #         code "$code_file"
    #     fi
    # elif [[ "$code_file" == *.java ]]; then
    #     # Java files -> IntelliJ IDEA
    #     if [[ -f "/mnt/c/Program Files/JetBrains/IntelliJ IDEA Community Edition 2024.3.4.1/bin/idea64.exe" ]]; then
    #         echo -e "${GREEN}☕ Opening Java file in IntelliJ IDEA...${NC}"
    #         "/mnt/c/Program Files/JetBrains/IntelliJ IDEA Community Edition 2024.3.4.1/bin/idea64.exe" "$code_file" &
    #     elif command -v code &> /dev/null; then
    #         echo -e "${YELLOW}📝 IntelliJ IDEA not found, opening in VS Code...${NC}"
    #         code "$code_file"
    #     fi
    # elif [[ -d "$(dirname "$code_file")/src" ]] || [[ -f "$(dirname "$code_file")/../build.gradle" ]]; then
    #     # Android project structure detected -> Android Studio
    #     if [[ -f "/mnt/t/Android/AndroidStudio/bin/studio64.exe" ]]; then
    #         echo -e "${GREEN}🤖 Android project detected, opening in Android Studio...${NC}"
    #         "/mnt/t/Android/AndroidStudio/bin/studio64.exe" "$(dirname "$code_file")/.." &
    #     fi
    # else
    #     # Fallback to VS Code
    #     if command -v code &> /dev/null; then
    #         echo -e "${GREEN}📝 Opening in VS Code...${NC}"
    #         code "$code_file"
    #     fi
    # fi
    
    # Step 6: Clear finish command instructions
    echo ""
    echo -e "${PURPLE}🎯 When you finish coding:${NC}"
    echo -e "${CYAN}   ./ai f ${NC}  ← Use this command ONLY"
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
    local note_file=""
    case "$task_id" in
        *"1.1."*|*"1.2."*|*"1.3."*)
            task_file="MICRO_TASK_C01.md"
            note_file="notes/jmm_notes_carrie.md"
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
    # local note_file="$notes_dir/${task_id//[^a-zA-Z0-9]/_}_notes.md"
    # 先截取掉最后一个点及其后面的所有内容，再把剩余的点替换成下划线
    local base_name="${task_id%.*}"
    note_file="$notes_dir/${base_name//./_}_notes.md"

    
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
    
    # === INTEGRATED LEARNING ANALYTICS SYSTEM ===
    echo ""
    echo -e "${BLUE}🧠 Running AI Learning Performance Analysis...${NC}"
    
    # Check if performance tracker exists
    local analytics_script="analytics/learning_performance_tracker.py"
    if [[ -f "$analytics_script" ]]; then
        # Call the learning performance tracker
        echo -e "${CYAN}🔍 Analyzing your learning performance...${NC}"
        
        # Run the performance analysis system
        python3 "$analytics_script" --student carrie --task "$task_id" --completion-time "$duration_minutes" --session-data "learning_data/learning_log.csv"
        
        if [[ $? -eq 0 ]]; then
            echo -e "${GREEN}✅ AI Performance Analysis completed successfully!${NC}"
            
            # Check if assessment report was generated
            local assessment_file="learning_data/reports/task_${task_id//\./_}_final_assessment.md"
            if [[ -f "$assessment_file" ]]; then
                echo -e "${BLUE}📊 AI Assessment Report generated: $assessment_file${NC}"
                
                # Show quick score summary if available
                local score=$(grep -o "[0-9]\+/100 (A[+-]\?)" "$assessment_file" | head -1)
                if [[ -n "$score" ]]; then
                    echo -e "${GREEN}🏆 Your AI-Generated Score: $score${NC}"
                fi
                
                # Open assessment report in VS Code if available
                if command -v code &> /dev/null; then
                    echo -e "${BLUE}📖 Opening AI assessment in VS Code...${NC}"
                    code "$assessment_file"
                fi
            fi
        else
            echo -e "${YELLOW}⚠️ AI Performance Analysis encountered an issue, but learning data was still saved${NC}"
        fi
    else
        echo -e "${YELLOW}⚠️ AI Performance Tracker not found at: $analytics_script${NC}"
        echo -e "${CYAN}💡 Your learning data is still being tracked in learning_log.csv${NC}"
    fi
    
    # Update performance database if available
    local performance_db="learning_data/performance_results.json"
    if [[ -f "$performance_db" ]]; then
        echo -e "${GREEN}📈 Performance database updated: $performance_db${NC}"
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
            echo -e "${CYAN}Quick start: ./ai s $next_task${NC}"
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
    # generate_quick_report
    generate_report
    show_calendar
}

# Generate quick learning report (adapted from simple_learning_tracker.sh)
generate_quick_report() {
    
    if [[ ! -f "$LOG_FILE" ]]; then
        echo -e "${YELLOW}⚠️ No learning data found yet${NC}"
        return
    fi
    
    # Basic statistics
    local total_sessions=$(tail -n +2 "$LOG_FILE" | wc -l)
    local total_minutes=$(tail -n +2 "$LOG_FILE" | cut -d',' -f4 | awk '{sum+=$1} END {print sum+0}')
    
    # This week's data
    # 一个更稳健的获取本周一日期的方法
    local day_of_week=$(date +%u) # 获取今天是周几 (1=周一, 7=周日)
    local days_to_subtract=$((day_of_week - 1))
    local current_week=$(date '+%Y-%m-%d' -d "-$days_to_subtract days")
    # local current_week=$(date '+%Y-%m-%d' -d 'monday')
    local this_week_sessions=$(tail -n +2 "$LOG_FILE" | awk -F',' -v week="$current_week" '$1 >= week' | wc -l)
    
    echo "═══════════════════════════════════════════════════════"
    echo -e "${YELLOW}🎯 Your Progress Summary:${NC}"
    echo "   📈 Total Sessions: $total_sessions"
    echo "   ⏰ Total Time: $total_minutes minutes ($(($total_minutes / 60))h $(($total_minutes % 60))m)"
    echo "   📅 This Week: $this_week_sessions sessions"
    
    # Show recent sessions
    if [[ $total_sessions -gt 0 ]]; then
        echo ""
        echo -e "${YELLOW}📊 Recent Learning:${NC}"
        tail -n 3 "$LOG_FILE" | while IFS=',' read -r date time task duration quality difficulty status notes task_file task_id; do
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
    # 一个更稳健的获取本周一日期的方法
    local day_of_week=$(date +%u) # 获取今天是周几 (1=周一, 7=周日)
    local days_to_subtract=$((day_of_week - 1))
    local current_week=$(date '+%Y-%m-%d' -d "-$days_to_subtract days")
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

make_prompt() {
    cat <<EOF
🔖 你的当前进度: $progress
📅 今日任务来自 TIMELINE.md: $today_line

💡 Prompt:
1. 阅读 micro_task 对应段落，先划出「概念要点」
2. 手写代码完成模板文件
3. 运行 ./ai f 触发 AI 评估
4. 在 PROGRESS.md 勾选完成项
EOF
}

# Main command dispatcher
case "${1:-help}" in
    "today"|"roadmap-today")
        initialize_progress
        show_todays_tasks
        make_prompt
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
        echo "  ./ai today              → Show today's recommended tasks"
        echo "  ./ai o 1.1.1            → Actually open and show task content"
        echo "  ./ai s 1.1.1            → Full setup: open file + create code + track"
        echo "  ./ai f                  → Complete current task"
        echo ""
        echo -e "${YELLOW}🛠️ Utility Commands:${NC}"
        echo "  ./ai w 1                → Create week directory structure -workspace"
        echo "  ./ai b                  → Browse available micro task files"
        echo ""
        echo -e "${CYAN}🎯 Example Workflow:${NC}"
        echo "  1. ./ai t               → See today's tasks"
        echo "  2. ./ai s 1.1.1     → Setup everything + open IDEs"
        echo "  3. [Code for 5-90 minutes in Android Studio/IntelliJ/VS Code]"
        echo "  4. ./ai f               → Complete and review (ONLY command to use)"
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