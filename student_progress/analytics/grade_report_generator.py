#!/usr/bin/env python3
"""
成绩报告生成器 - 生成阶段性学习评估和章节成绩
"""

import json
from datetime import datetime
from pathlib import Path

class GradeReportGenerator:
    """生成详细的学习成绩报告和阶段性评估"""
    
    def __init__(self):
        self.performance_file = Path("../learning_data/performance_results.json")
        
    def load_data(self):
        """加载性能数据"""
        try:
            with open(self.performance_file, 'r', encoding='utf-8') as f:
                return json.load(f)
        except FileNotFoundError:
            return {}
    
    def generate_current_progress_report(self):
        """生成当前学习进度报告"""
        data = self.load_data()
        
        report = {
            "report_date": datetime.now().isoformat()[:10],
            "report_type": "progress_snapshot",
            "student_profile": self._extract_student_summary(data),
            "current_performance": self._analyze_current_performance(data),
            "chapter_progress": self._analyze_chapter_progress(data),
            "achievements": self._summarize_achievements(data),
            "learning_trends": self._analyze_trends(data),
            "recommendations": self._generate_recommendations(data)
        }
        
        return report
    
    def generate_chapter_completion_report(self, chapter_id):
        """生成章节完成报告"""
        data = self.load_data()
        chapter_key = f"{chapter_id}_concurrent_fundamentals" if chapter_id == "c01" else f"{chapter_id}_chapter"
        chapter_data = data.get("chapter_summaries", {}).get(chapter_key, {})
        
        if not chapter_data:
            return {"error": "Chapter data not found"}
        
        completion_rate = chapter_data.get("completion_rate", 0)
        if completion_rate < 100:
            return {"status": "in_progress", "completion_rate": completion_rate}
        
        # 生成完整的章节成绩报告
        report = {
            "report_date": datetime.now().isoformat()[:10],
            "report_type": "chapter_completion",
            "chapter_info": {
                "chapter_id": chapter_id,
                "chapter_name": chapter_data.get("chapter_name", ""),
                "completion_date": datetime.now().isoformat()[:10]
            },
            "final_grades": self._calculate_chapter_grades(data, chapter_id),
            "skill_development": self._assess_skill_development(data, chapter_id),
            "concept_mastery": self._evaluate_concept_mastery(data, chapter_id),
            "time_analysis": self._analyze_time_efficiency(data, chapter_id),
            "achievement_highlights": self._highlight_achievements(data, chapter_id),
            "areas_of_excellence": self._identify_excellence_areas(data, chapter_id),
            "growth_trajectory": self._analyze_growth(data, chapter_id),
            "next_chapter_readiness": self._assess_readiness(data, chapter_id)
        }
        
        return report
    
    def _extract_student_summary(self, data):
        """提取学生概况"""
        profile = data.get("student_profile", {})
        progress = profile.get("overall_progress", {})
        
        return {
            "student_id": profile.get("student_id", ""),
            "learning_duration": self._calculate_learning_duration(profile.get("start_date", "")),
            "current_skill_level": progress.get("current_skill_level", "beginner"),
            "overall_average": progress.get("overall_score_average", 0),
            "tasks_completed": progress.get("total_tasks_completed", 0),
            "learning_velocity": progress.get("learning_velocity", "medium")
        }
    
    def _analyze_current_performance(self, data):
        """分析当前表现"""
        tasks = data.get("detailed_task_history", [])
        if not tasks:
            return {"status": "no_data"}
        
        latest_task = tasks[-1]
        metrics = latest_task.get("performance_metrics", {})
        
        return {
            "latest_task": {
                "task_name": latest_task.get("task_name", ""),
                "score": metrics.get("overall_score", 0),
                "date": latest_task.get("timestamp", "")[:10]
            },
            "performance_breakdown": {
                "concept_understanding": metrics.get("concept_understanding_score", 0),
                "code_quality": metrics.get("code_quality_score", 0),
                "problem_solving": metrics.get("problem_solving", 0),
                "experimental_thinking": metrics.get("experimental_thinking", 0)
            },
            "performance_level": self._classify_performance_level(metrics.get("overall_score", 0))
        }
    
    def _analyze_chapter_progress(self, data):
        """分析章节进度"""
        chapters = data.get("chapter_summaries", {})
        progress_summary = {}
        
        for chapter_id, chapter_data in chapters.items():
            progress_summary[chapter_id] = {
                "name": chapter_data.get("chapter_name", ""),
                "completion_rate": chapter_data.get("completion_rate", 0),
                "current_grade": chapter_data.get("chapter_grade", "N/A"),
                "tasks_completed": chapter_data.get("tasks_completed", 0),
                "total_tasks": chapter_data.get("total_tasks", 0),
                "key_concepts": len(chapter_data.get("key_concepts_mastered", [])),
                "time_investment": chapter_data.get("time_spent_minutes", 0)
            }
        
        return progress_summary
    
    def _summarize_achievements(self, data):
        """汇总成就"""
        achievements = data.get("achievements_unlocked", [])
        
        summary = {
            "total_count": len(achievements),
            "total_points": sum(a.get("points", 0) for a in achievements),
            "by_rarity": {},
            "recent_achievements": []
        }
        
        # 按稀有度分类
        for achievement in achievements:
            rarity = achievement.get("rarity", "common")
            if rarity not in summary["by_rarity"]:
                summary["by_rarity"][rarity] = 0
            summary["by_rarity"][rarity] += 1
        
        # 最近成就
        recent = sorted(achievements, key=lambda x: x.get("earned_date", ""), reverse=True)[:3]
        summary["recent_achievements"] = [
            {"title": a.get("title", ""), "rarity": a.get("rarity", ""), "points": a.get("points", 0)}
            for a in recent
        ]
        
        return summary
    
    def _analyze_trends(self, data):
        """分析学习趋势"""
        analytics = data.get("learning_analytics", {})
        trends = analytics.get("performance_trends", {})
        
        return {
            "score_trend": trends.get("score_trend", "stable"),
            "learning_velocity": trends.get("learning_velocity", "medium"),
            "concept_retention": trends.get("concept_retention", "good"),
            "skill_progression": self._analyze_skill_progression(analytics)
        }
    
    def _analyze_skill_progression(self, analytics):
        """分析技能进展"""
        trajectory = analytics.get("learning_trajectory", {})
        progression = trajectory.get("skill_progression", [])
        
        if len(progression) < 2:
            return "insufficient_data"
        
        start_level = progression[0].get("skill_level", "beginner")
        current_level = progression[-1].get("skill_level", "beginner")
        
        level_map = {"beginner": 1, "beginner_plus": 2, "intermediate": 3, "advanced": 4, "expert": 5}
        start_score = level_map.get(start_level, 1)
        current_score = level_map.get(current_level, 1)
        
        if current_score > start_score:
            return "accelerated_growth"
        elif current_score == start_score:
            return "stable_development"
        else:
            return "needs_attention"
    
    def _generate_recommendations(self, data):
        """生成个性化建议"""
        recommendations = []
        
        # 基于整体表现
        overall_avg = data.get("student_profile", {}).get("overall_progress", {}).get("overall_score_average", 0)
        
        if overall_avg >= 90:
            recommendations.append({
                "type": "advancement",
                "priority": "high",
                "suggestion": "表现卓越！建议挑战更高难度的专项任务",
                "rationale": f"平均分{overall_avg}分显示出强大的学习能力"
            })
        elif overall_avg >= 80:
            recommendations.append({
                "type": "enhancement", 
                "priority": "medium",
                "suggestion": "继续保持良好的学习节奏，可适当增加实践项目",
                "rationale": f"平均分{overall_avg}分表现良好"
            })
        else:
            recommendations.append({
                "type": "consolidation",
                "priority": "high", 
                "suggestion": "建议巩固基础概念，适当放慢学习节奏",
                "rationale": f"平均分{overall_avg}分需要加强基础"
            })
        
        # 基于学习速度
        velocity = data.get("student_profile", {}).get("overall_progress", {}).get("learning_velocity", "medium")
        if velocity == "accelerated":
            recommendations.append({
                "type": "challenge",
                "priority": "medium",
                "suggestion": "学习速度很快，可以尝试跨章节的综合性挑战",
                "rationale": "加速学习模式适合更复杂的任务"
            })
        
        return recommendations
    
    def _calculate_learning_duration(self, start_date):
        """计算学习持续时间"""
        if not start_date:
            return "unknown"
        
        try:
            start = datetime.fromisoformat(start_date)
            now = datetime.now()
            delta = now - start
            return f"{delta.days}天"
        except:
            return "unknown"
    
    def _classify_performance_level(self, score):
        """分类表现水平"""
        if score >= 95:
            return "exceptional"
        elif score >= 90:
            return "excellent"  
        elif score >= 80:
            return "good"
        elif score >= 70:
            return "satisfactory"
        elif score >= 60:
            return "needs_improvement"
        else:
            return "requires_attention"
    
    def save_report(self, report, filename):
        """保存报告到文件"""
        report_file = Path("../learning_data/reports") / filename
        report_file.parent.mkdir(parents=True, exist_ok=True)
        
        with open(report_file, 'w', encoding='utf-8') as f:
            json.dump(report, f, ensure_ascii=False, indent=2)
        
        return str(report_file)

def generate_current_report():
    """生成当前进度报告"""
    generator = GradeReportGenerator()
    report = generator.generate_current_progress_report()
    
    filename = f"progress_report_{datetime.now().strftime('%Y%m%d')}.json"
    report_file = generator.save_report(report, filename)
    
    return report, report_file

def generate_chapter_report(chapter_id):
    """生成章节完成报告"""
    generator = GradeReportGenerator()
    report = generator.generate_chapter_completion_report(chapter_id)
    
    if "error" in report or "status" in report:
        return report, None
    
    filename = f"chapter_{chapter_id}_completion_{datetime.now().strftime('%Y%m%d')}.json" 
    report_file = generator.save_report(report, filename)
    
    return report, report_file

if __name__ == "__main__":
    # 生成当前进度报告
    report, file_path = generate_current_report()
    print(f"✅ 当前学习进度报告已生成: {file_path}")
    print(f"📊 整体表现: {report['current_performance']['performance_level']}")
    print(f"🎯 技能水平: {report['student_profile']['current_skill_level']}")
    print(f"⭐ 平均分: {report['student_profile']['overall_average']:.1f}/100")