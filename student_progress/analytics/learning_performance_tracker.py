#!/usr/bin/env python3
"""
学习性能跟踪系统
自动记录、分析和报告学员的学习进展和成绩
"""

import json
import datetime
from pathlib import Path
from typing import Dict, List, Any, Optional

class LearningPerformanceTracker:
    """
    学习性能跟踪器 - 完整的学习分析和评估系统
    """
    
    def __init__(self, base_path: str = None):
        if base_path:
            self.base_path = Path(base_path)
        else:
            self.base_path = Path(__file__).parent.parent
        
        self.performance_file = self.base_path / "learning_data" / "performance_results.json"
        self.ensure_data_directory()
    
    def ensure_data_directory(self):
        """确保数据目录存在"""
        self.performance_file.parent.mkdir(parents=True, exist_ok=True)
        
        # 如果文件不存在，创建初始结构
        if not self.performance_file.exists():
            self._create_initial_structure()
    
    def _create_initial_structure(self):
        """创建初始的性能跟踪文件结构"""
        initial_data = {
            "student_profile": {
                "student_id": "carrie",
                "start_date": datetime.datetime.now().strftime("%Y-%m-%d"),
                "learning_approach": "adhd_friendly_micro_tasks",
                "current_chapter": "c01",
                "overall_progress": {
                    "total_tasks_attempted": 0,
                    "total_tasks_completed": 0,
                    "current_skill_level": "beginner",
                    "overall_score_average": 0.0
                }
            },
            "chapter_summaries": {},
            "detailed_task_history": [],
            "learning_analytics": {
                "learning_trajectory": {
                    "skill_progression": [],
                    "concept_mastery_timeline": []
                },
                "performance_trends": {
                    "score_trend": "stable",
                    "learning_velocity": "medium",
                    "concept_retention": "good",
                    "practical_application": "developing"
                }
            },
            "achievements_unlocked": [],
            "mentor_notes": {}
        }
        
        with open(self.performance_file, 'w', encoding='utf-8') as f:
            json.dump(initial_data, f, ensure_ascii=False, indent=2)
    
    def load_performance_data(self) -> Dict:
        """加载性能数据"""
        try:
            with open(self.performance_file, 'r', encoding='utf-8') as f:
                return json.load(f)
        except Exception as e:
            print(f"加载性能数据失败: {e}")
            return {}
    
    def save_performance_data(self, data: Dict):
        """保存性能数据"""
        try:
            with open(self.performance_file, 'w', encoding='utf-8') as f:
                json.dump(data, f, ensure_ascii=False, indent=2)
            print(f"✅ 学习数据已保存到: {self.performance_file}")
        except Exception as e:
            print(f"❌ 保存性能数据失败: {e}")
    
    def record_task_completion(self, task_data: Dict):
        """记录任务完成情况"""
        data = self.load_performance_data()
        
        # 添加任务记录
        task_record = {
            "timestamp": datetime.datetime.now().isoformat(),
            "session_id": f"session_{len(data.get('detailed_task_history', [])) + 1:03d}",
            **task_data
        }
        
        if 'detailed_task_history' not in data:
            data['detailed_task_history'] = []
        data['detailed_task_history'].append(task_record)
        
        # 更新整体进度
        self._update_overall_progress(data, task_record)
        
        # 更新章节总结
        self._update_chapter_summary(data, task_record)
        
        # 更新学习分析
        self._update_learning_analytics(data, task_record)
        
        # 检查成就解锁
        self._check_achievements(data, task_record)
        
        self.save_performance_data(data)
        return task_record
    
    def _update_overall_progress(self, data: Dict, task_record: Dict):
        """更新整体学习进度"""
        overall = data.setdefault('student_profile', {}).setdefault('overall_progress', {})
        
        overall['total_tasks_attempted'] = overall.get('total_tasks_attempted', 0) + 1
        
        if task_record.get('performance_metrics', {}).get('overall_score', 0) >= 60:
            overall['total_tasks_completed'] = overall.get('total_tasks_completed', 0) + 1
        
        # 更新平均分数
        history = data.get('detailed_task_history', [])
        if history:
            total_score = sum(task.get('performance_metrics', {}).get('overall_score', 0) 
                            for task in history)
            overall['overall_score_average'] = total_score / len(history)
        
        # 更新技能等级
        avg_score = overall.get('overall_score_average', 0)
        if avg_score >= 90:
            overall['current_skill_level'] = "advanced"
        elif avg_score >= 75:
            overall['current_skill_level'] = "intermediate"
        elif avg_score >= 60:
            overall['current_skill_level'] = "beginner_plus"
        else:
            overall['current_skill_level'] = "beginner"
    
    def _update_chapter_summary(self, data: Dict, task_record: Dict):
        """更新章节总结"""
        chapter = task_record.get('chapter', 'unknown')
        chapter_key = f"{chapter}_concurrent_fundamentals" if chapter == "c01" else f"{chapter}_chapter"
        
        summaries = data.setdefault('chapter_summaries', {})
        chapter_summary = summaries.setdefault(chapter_key, {
            "chapter_name": self._get_chapter_name(chapter),
            "status": "in_progress",
            "tasks_completed": 0,
            "total_tasks": self._get_chapter_total_tasks(chapter),
            "completion_rate": 0.0,
            "average_score": 0.0,
            "time_spent_minutes": 0,
            "key_concepts_mastered": [],
            "areas_for_improvement": [],
            "chapter_grade": "N/A",
            "started_date": datetime.datetime.now().strftime("%Y-%m-%d"),
            "last_activity": datetime.datetime.now().isoformat()
        })
        
        # 更新章节数据
        if task_record.get('performance_metrics', {}).get('overall_score', 0) >= 60:
            chapter_summary['tasks_completed'] += 1
        
        chapter_summary['completion_rate'] = (chapter_summary['tasks_completed'] / 
                                            chapter_summary['total_tasks'] * 100)
        
        # 计算章节平均分
        chapter_tasks = [task for task in data.get('detailed_task_history', []) 
                        if task.get('chapter') == chapter]
        if chapter_tasks:
            total_score = sum(task.get('performance_metrics', {}).get('overall_score', 0) 
                            for task in chapter_tasks)
            chapter_summary['average_score'] = total_score / len(chapter_tasks)
        
        # 更新时间统计
        chapter_summary['time_spent_minutes'] += task_record.get('time_analysis', {}).get('total_time_minutes', 0)
        
        # 更新掌握的概念
        concepts = task_record.get('detailed_analysis', {}).get('concept_mastery', {}).get('concepts_understood', {})
        for concept, details in concepts.items():
            if details.get('level') in ['intermediate', 'advanced']:
                concept_name = self._translate_concept_name(concept)
                if concept_name not in chapter_summary['key_concepts_mastered']:
                    chapter_summary['key_concepts_mastered'].append(concept_name)
        
        # 更新章节成绩
        avg_score = chapter_summary.get('average_score', 0)
        if avg_score >= 95:
            chapter_summary['chapter_grade'] = "A+"
        elif avg_score >= 90:
            chapter_summary['chapter_grade'] = "A"
        elif avg_score >= 85:
            chapter_summary['chapter_grade'] = "B+"
        elif avg_score >= 80:
            chapter_summary['chapter_grade'] = "B"
        elif avg_score >= 75:
            chapter_summary['chapter_grade'] = "C+"
        elif avg_score >= 70:
            chapter_summary['chapter_grade'] = "C"
        else:
            chapter_summary['chapter_grade'] = "D"
        
        chapter_summary['last_activity'] = datetime.datetime.now().isoformat()
    
    def _update_learning_analytics(self, data: Dict, task_record: Dict):
        """更新学习分析数据"""
        analytics = data.setdefault('learning_analytics', {})
        trajectory = analytics.setdefault('learning_trajectory', {})
        
        # 更新技能进展
        skill_progression = trajectory.setdefault('skill_progression', [])
        current_skill = data.get('student_profile', {}).get('overall_progress', {}).get('current_skill_level', 'beginner')
        
        if not skill_progression or skill_progression[-1]['skill_level'] != current_skill:
            skill_progression.append({
                "date": datetime.datetime.now().strftime("%Y-%m-%d"),
                "skill_level": current_skill,
                "key_milestone": task_record.get('task_name', '未知任务')
            })
        
        # 更新概念掌握时间线
        concept_timeline = trajectory.setdefault('concept_mastery_timeline', [])
        concepts = task_record.get('detailed_analysis', {}).get('concept_mastery', {}).get('concepts_understood', {})
        
        for concept, details in concepts.items():
            if details.get('level') in ['intermediate', 'advanced']:
                concept_timeline.append({
                    "date": datetime.datetime.now().strftime("%Y-%m-%d"),
                    "concept": self._translate_concept_name(concept),
                    "mastery_level": details.get('level'),
                    "evidence": details.get('evidence', '')
                })
        
        # 更新性能趋势
        trends = analytics.setdefault('performance_trends', {})
        history = data.get('detailed_task_history', [])
        
        if len(history) >= 2:
            recent_scores = [task.get('performance_metrics', {}).get('overall_score', 0) 
                           for task in history[-3:]]
            if len(recent_scores) >= 2:
                if recent_scores[-1] > recent_scores[0]:
                    trends['score_trend'] = "increasing"
                elif recent_scores[-1] < recent_scores[0]:
                    trends['score_trend'] = "decreasing"
                else:
                    trends['score_trend'] = "stable"
        
        # 评估学习速度
        time_efficiency = task_record.get('time_analysis', {}).get('efficiency_rating', 'medium')
        if time_efficiency == 'high':
            trends['learning_velocity'] = "high"
        elif time_efficiency == 'low':
            trends['learning_velocity'] = "low"
        else:
            trends['learning_velocity'] = "medium"
    
    def _check_achievements(self, data: Dict, task_record: Dict):
        """检查并解锁成就"""
        achievements = data.setdefault('achievements_unlocked', [])
        score = task_record.get('performance_metrics', {}).get('overall_score', 0)
        
        # 基础成就
        if score >= 90 and not self._has_achievement(achievements, 'high_performer'):
            achievements.append({
                "achievement_id": "high_performer",
                "title": "高分达成者",
                "description": "单次任务得分超过90分",
                "rarity": "uncommon",
                "earned_date": datetime.datetime.now().strftime("%Y-%m-%d"),
                "points": 100
            })
        
        # 特定概念成就
        concepts = task_record.get('detailed_analysis', {}).get('concept_mastery', {}).get('concepts_understood', {})
        
        if 'jmm_visibility' in concepts and not self._has_achievement(achievements, 'jmm_explorer'):
            achievements.append({
                "achievement_id": "jmm_explorer",
                "title": "JMM探索者",
                "description": "成功理解并实现JMM可见性问题",
                "rarity": "rare",
                "earned_date": datetime.datetime.now().strftime("%Y-%m-%d"),
                "points": 200
            })
        
        if 'volatile_semantics' in concepts and not self._has_achievement(achievements, 'volatile_master'):
            achievements.append({
                "achievement_id": "volatile_master", 
                "title": "volatile大师",
                "description": "准确应用volatile解决并发问题",
                "rarity": "rare",
                "earned_date": datetime.datetime.now().strftime("%Y-%m-%d"),
                "points": 250
            })
    
    def _has_achievement(self, achievements: List, achievement_id: str) -> bool:
        """检查是否已有某个成就"""
        return any(a.get('achievement_id') == achievement_id for a in achievements)
    
    def _get_chapter_name(self, chapter: str) -> str:
        """获取章节名称"""
        chapter_names = {
            "c01": "并发编程基础",
            "c02": "集合框架实现", 
            "c03": "协程系统掌握",
            "c04": "性能优化技巧"
        }
        return chapter_names.get(chapter, f"第{chapter}章")
    
    def _get_chapter_total_tasks(self, chapter: str) -> int:
        """获取章节总任务数"""
        task_counts = {
            "c01": 16,
            "c02": 13,
            "c03": 14, 
            "c04": 10
        }
        return task_counts.get(chapter, 10)
    
    def _translate_concept_name(self, concept: str) -> str:
        """翻译概念名称"""
        translations = {
            "jmm_visibility": "JMM内存可见性",
            "volatile_semantics": "volatile语义",
            "thread_lifecycle": "线程生命周期",
            "happens_before": "happens-before关系",
            "synchronization": "同步机制"
        }
        return translations.get(concept, concept)
    
    def generate_progress_report(self) -> Dict:
        """生成学习进度报告"""
        data = self.load_performance_data()
        
        report = {
            "report_date": datetime.datetime.now().isoformat(),
            "student_summary": data.get('student_profile', {}),
            "chapter_progress": {},
            "recent_performance": [],
            "achievements_summary": {},
            "recommendations": []
        }
        
        # 章节进度摘要
        for chapter_id, chapter_data in data.get('chapter_summaries', {}).items():
            report['chapter_progress'][chapter_id] = {
                "name": chapter_data.get('chapter_name', ''),
                "completion_rate": chapter_data.get('completion_rate', 0),
                "grade": chapter_data.get('chapter_grade', 'N/A'),
                "concepts_mastered": len(chapter_data.get('key_concepts_mastered', []))
            }
        
        # 最近表现
        recent_tasks = data.get('detailed_task_history', [])[-5:]  # 最近5个任务
        for task in recent_tasks:
            report['recent_performance'].append({
                "task": task.get('task_name', ''),
                "score": task.get('performance_metrics', {}).get('overall_score', 0),
                "date": task.get('timestamp', '')[:10]  # 只要日期部分
            })
        
        # 成就摘要
        achievements = data.get('achievements_unlocked', [])
        report['achievements_summary'] = {
            "total_achievements": len(achievements),
            "total_points": sum(a.get('points', 0) for a in achievements),
            "recent_achievements": [a.get('title', '') for a in achievements[-3:]]
        }
        
        # 个性化建议
        avg_score = data.get('student_profile', {}).get('overall_progress', {}).get('overall_score_average', 0)
        if avg_score >= 85:
            report['recommendations'].append("表现优异！可以尝试更有挑战性的高级任务")
        elif avg_score >= 70:
            report['recommendations'].append("进步稳定，继续保持当前学习节奏")
        else:
            report['recommendations'].append("建议复习已学概念，巩固基础后再继续")
        
        return report
    
    def get_chapter_grade(self, chapter: str) -> Dict:
        """获取章节成绩"""
        data = self.load_performance_data()
        chapter_key = f"{chapter}_concurrent_fundamentals" if chapter == "c01" else f"{chapter}_chapter"
        
        chapter_data = data.get('chapter_summaries', {}).get(chapter_key, {})
        
        return {
            "chapter": chapter,
            "chapter_name": chapter_data.get('chapter_name', '未知章节'),
            "completion_rate": chapter_data.get('completion_rate', 0),
            "average_score": chapter_data.get('average_score', 0),
            "grade": chapter_data.get('chapter_grade', 'N/A'),
            "concepts_mastered": chapter_data.get('key_concepts_mastered', []),
            "time_spent": chapter_data.get('time_spent_minutes', 0),
            "status": chapter_data.get('status', 'not_started')
        }

# 便捷函数
def record_task_result(task_data: Dict):
    """记录任务结果的便捷函数"""
    tracker = LearningPerformanceTracker()
    return tracker.record_task_completion(task_data)

def get_progress_report():
    """获取学习进度报告的便捷函数"""
    tracker = LearningPerformanceTracker()
    return tracker.generate_progress_report()

def get_chapter_summary(chapter: str):
    """获取章节总结的便捷函数"""
    tracker = LearningPerformanceTracker()
    return tracker.get_chapter_grade(chapter)

if __name__ == "__main__":
    # 示例：记录Task 1.1.1的结果
    sample_task_data = {
        "task_id": "1.1.1",
        "task_name": "JMM概念入门+首个编程实验",
        "chapter": "c01",
        "performance_metrics": {
            "overall_score": 90.0,
            "code_quality_score": 85.0,
            "concept_understanding_score": 95.0
        },
        "detailed_analysis": {
            "concept_mastery": {
                "concepts_understood": {
                    "jmm_visibility": {"level": "advanced", "evidence": "成功实现可见性问题演示"},
                    "volatile_semantics": {"level": "advanced", "evidence": "正确应用volatile解决问题"}
                }
            }
        },
        "time_analysis": {
            "total_time_minutes": 45,
            "efficiency_rating": "high"
        }
    }
    
    result = record_task_result(sample_task_data)
    print("✅ 任务结果已记录")
    print(json.dumps(result, ensure_ascii=False, indent=2))