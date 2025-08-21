#!/usr/bin/env python3
"""
代码质量检查器 - 集成学习分析系统
用于分析学员手动编写的代码质量和理解程度
"""

import os
import re
import json
import datetime
from pathlib import Path

class CodeQualityChecker:
    """
    代码质量检查器
    分析学员代码的质量、理解程度和学习进展
    """
    
    def __init__(self):
        self.base_path = Path(__file__).parent.parent
        self.student_code_path = self.base_path / "student_code"
        self.demo_code_path = self.base_path / "demo_code"
        
    def analyze_student_code(self, student_id: str, chapter: str, task_file: str):
        """
        分析学员代码质量
        """
        student_file = self.student_code_path / chapter / task_file
        demo_file = self.demo_code_path / chapter / task_file
        
        if not student_file.exists():
            return {
                "status": "not_found",
                "message": "学员代码文件不存在，请先完成编程任务",
                "recommendations": [
                    "查看演示代码理解任务要求",
                    "手动创建并编写代码文件",
                    "确保文件保存在正确位置"
                ]
            }
            
        # 分析代码质量
        quality_analysis = self._analyze_code_quality(student_file, demo_file)
        
        # 分析概念理解
        concept_analysis = self._analyze_concept_understanding(student_file, task_file)
        
        # 生成学习报告
        learning_report = self._generate_learning_report(
            student_id, chapter, task_file, quality_analysis, concept_analysis
        )
        
        return learning_report
        
    def _analyze_code_quality(self, student_file, demo_file):
        """
        分析代码质量指标
        """
        try:
            with open(student_file, 'r', encoding='utf-8') as f:
                student_code = f.read()
        except Exception as e:
            return {"error": f"无法读取学员代码: {str(e)}"}
            
        quality_metrics = {
            "code_length": len(student_code.split('\n')),
            "has_comments": len(re.findall(r'//.*|/\*.*?\*/', student_code, re.DOTALL)) > 0,
            "proper_naming": self._check_naming_conventions(student_code),
            "exception_handling": 'try' in student_code and 'catch' in student_code,
            "thread_usage": 'Thread' in student_code or 'Runnable' in student_code,
            "synchronization": 'synchronized' in student_code or 'volatile' in student_code,
            "compilation_ready": self._check_basic_syntax(student_code)
        }
        
        # 计算质量分数
        quality_score = self._calculate_quality_score(quality_metrics)
        
        return {
            "metrics": quality_metrics,
            "quality_score": quality_score,
            "areas_for_improvement": self._identify_improvement_areas(quality_metrics)
        }
        
    def _analyze_concept_understanding(self, student_file, task_file):
        """
        分析概念理解程度
        """
        try:
            with open(student_file, 'r', encoding='utf-8') as f:
                student_code = f.read()
        except Exception:
            return {"understanding_level": "unknown"}
            
        concept_indicators = {}
        
        # 根据不同任务分析特定概念
        if "MemoryVisibilityDemo" in task_file:
            concept_indicators.update({
                "understands_visibility": 'flag' in student_code and 'counter' in student_code,
                "creates_reader_thread": 'while (!flag)' in student_code,
                "creates_writer_thread": 'flag = true' in student_code,
                "observes_problem": 'interrupt' in student_code or 'isAlive' in student_code,
                "explains_jmm": len(re.findall(r'//.*[Jj][Mm][Mm]|//.*可见性|//.*内存', student_code)) > 0
            })
            
        elif "HappensBeforeDemo" in task_file:
            concept_indicators.update({
                "understands_happens_before": 'volatile' in student_code,
                "compares_scenarios": 'testWithout' in student_code and 'testWithVolatile' in student_code,
                "demonstrates_problem": 'sharedData' in student_code and 'dataReady' in student_code,
                "explains_solution": len(re.findall(r'//.*volatile|//.*happens.before', student_code)) > 0
            })
            
        elif "SynchronizedDemo" in task_file:
            concept_indicators.update({
                "shows_race_condition": 'unsafeCounter' in student_code,
                "implements_synchronization": 'synchronized' in student_code,
                "measures_performance": 'System.currentTimeMillis' in student_code,
                "compares_results": 'safeCounter' in student_code and 'unsafeCounter' in student_code
            })
            
        elif "VolatileDemo" in task_file:
            concept_indicators.update({
                "distinguishes_volatile": 'volatile' in student_code,
                "shows_visibility": 'normalFlag' in student_code and 'volatileFlag' in student_code,
                "demonstrates_atomicity": 'volatileCounter++' in student_code,
                "explains_limitations": len(re.findall(r'//.*原子性|//.*atomicity', student_code)) > 0
            })
            
        understanding_level = self._calculate_understanding_level(concept_indicators)
        
        return {
            "concept_indicators": concept_indicators,
            "understanding_level": understanding_level,
            "conceptual_strengths": self._identify_strengths(concept_indicators),
            "conceptual_gaps": self._identify_gaps(concept_indicators)
        }
        
    def _generate_learning_report(self, student_id, chapter, task_file, quality_analysis, concept_analysis):
        """
        生成综合学习报告
        """
        timestamp = datetime.datetime.now().isoformat()
        
        # 综合评估
        overall_score = (quality_analysis.get("quality_score", 0) + 
                        self._concept_to_score(concept_analysis.get("understanding_level", "beginner"))) / 2
        
        report = {
            "student_id": student_id,
            "chapter": chapter,
            "task": task_file,
            "timestamp": timestamp,
            "overall_score": overall_score,
            "quality_analysis": quality_analysis,
            "concept_analysis": concept_analysis,
            "recommendations": self._generate_recommendations(quality_analysis, concept_analysis),
            "next_steps": self._suggest_next_steps(overall_score, task_file),
            "encouragement": self._generate_encouragement(overall_score)
        }
        
        # 保存学习记录
        self._save_learning_record(report)
        
        return report
        
    def _check_naming_conventions(self, code):
        """检查命名规范"""
        # 检查类名、方法名、变量名的基本规范
        class_names = re.findall(r'class\s+([A-Z][a-zA-Z0-9]*)', code)
        method_names = re.findall(r'(public|private|protected).*?([a-z][a-zA-Z0-9]*)\s*\(', code)
        
        return len(class_names) > 0 and len(method_names) > 0
        
    def _check_basic_syntax(self, code):
        """检查基本语法完整性"""
        # 简单检查括号匹配
        open_braces = code.count('{')
        close_braces = code.count('}')
        open_parens = code.count('(') 
        close_parens = code.count(')')
        
        return (open_braces == close_braces and 
                open_parens == close_parens and
                'public class' in code and
                'public static void main' in code)
        
    def _calculate_quality_score(self, metrics):
        """计算代码质量分数"""
        score = 0
        total_checks = len(metrics)
        
        for key, value in metrics.items():
            if isinstance(value, bool) and value:
                score += 1
            elif key == "code_length" and value > 20:  # 代码有一定长度
                score += 1
                
        return (score / total_checks) * 100
        
    def _calculate_understanding_level(self, indicators):
        """计算概念理解水平"""
        if not indicators:
            return "unknown"
            
        correct_count = sum(1 for v in indicators.values() if v)
        total_count = len(indicators)
        
        if total_count == 0:
            return "unknown"
            
        ratio = correct_count / total_count
        
        if ratio >= 0.8:
            return "advanced"
        elif ratio >= 0.6:
            return "intermediate"
        elif ratio >= 0.4:
            return "beginner"
        else:
            return "needs_help"
            
    def _concept_to_score(self, level):
        """概念理解水平转换为分数"""
        levels = {
            "advanced": 90,
            "intermediate": 75, 
            "beginner": 60,
            "needs_help": 40,
            "unknown": 50
        }
        return levels.get(level, 50)
        
    def _identify_improvement_areas(self, metrics):
        """识别需要改进的领域"""
        areas = []
        
        if not metrics.get("has_comments", False):
            areas.append("添加代码注释说明你的理解")
            
        if not metrics.get("proper_naming", False):
            areas.append("遵循Java命名规范")
            
        if not metrics.get("exception_handling", False):
            areas.append("添加异常处理机制")
            
        if not metrics.get("compilation_ready", False):
            areas.append("检查语法错误，确保代码可编译")
            
        return areas
        
    def _identify_strengths(self, indicators):
        """识别概念理解优势"""
        strengths = []
        for key, value in indicators.items():
            if value:
                strength_map = {
                    "understands_visibility": "理解JMM可见性问题",
                    "understands_happens_before": "掌握happens-before关系",
                    "implements_synchronization": "正确实现同步机制", 
                    "distinguishes_volatile": "理解volatile特性"
                }
                if key in strength_map:
                    strengths.append(strength_map[key])
        return strengths
        
    def _identify_gaps(self, indicators):
        """识别概念理解缺口"""
        gaps = []
        for key, value in indicators.items():
            if not value:
                gap_map = {
                    "understands_visibility": "需要加深对JMM可见性的理解",
                    "creates_reader_thread": "读取者线程实现不完整",
                    "explains_jmm": "缺乏对JMM原理的解释",
                    "compares_scenarios": "缺少对比实验"
                }
                if key in gap_map:
                    gaps.append(gap_map[key])
        return gaps
        
    def _generate_recommendations(self, quality_analysis, concept_analysis):
        """生成个性化建议"""
        recommendations = []
        
        # 基于代码质量的建议
        recommendations.extend(quality_analysis.get("areas_for_improvement", []))
        
        # 基于概念理解的建议
        gaps = concept_analysis.get("conceptual_gaps", [])
        for gap in gaps:
            recommendations.append(f"建议: {gap}")
            
        # 通用建议
        understanding_level = concept_analysis.get("understanding_level", "beginner")
        if understanding_level == "needs_help":
            recommendations.append("建议重新阅读演示代码和理论说明")
            recommendations.append("可以请求AI导师进行详细解释")
        elif understanding_level == "beginner":
            recommendations.append("继续练习，加深对并发概念的理解")
        elif understanding_level == "intermediate":
            recommendations.append("尝试修改代码参数，观察不同现象")
        elif understanding_level == "advanced":
            recommendations.append("可以尝试实现更复杂的并发场景")
            
        return recommendations[:5]  # 限制建议数量
        
    def _suggest_next_steps(self, overall_score, current_task):
        """建议下一步学习内容"""
        if overall_score >= 80:
            return f"出色完成{current_task}！可以继续下一个任务"
        elif overall_score >= 60:
            return f"基本掌握{current_task}，建议再次练习后继续"
        else:
            return f"需要重新学习{current_task}的相关概念"
            
    def _generate_encouragement(self, score):
        """生成鼓励性反馈"""
        if score >= 90:
            return "🎉 优秀！你对并发编程的理解很深入！"
        elif score >= 80:
            return "👏 很好！继续保持这样的学习态度！"
        elif score >= 70:
            return "💪 不错的进步！继续努力！"
        elif score >= 60:
            return "🌱 正在进步中，坚持下去！"
        else:
            return "🤗 学习需要时间，不要放弃！每一步都有价值！"
            
    def _save_learning_record(self, report):
        """保存学习记录"""
        records_dir = self.base_path / "learning_data" / "analytics"
        records_dir.mkdir(parents=True, exist_ok=True)
        
        filename = f"{report['student_id']}_{report['chapter']}_{report['task']}_{datetime.datetime.now().strftime('%Y%m%d_%H%M%S')}.json"
        
        with open(records_dir / filename, 'w', encoding='utf-8') as f:
            json.dump(report, f, ensure_ascii=False, indent=2)

def check_student_code(student_id: str, chapter: str, task_file: str):
    """
    便捷函数：检查学员代码
    """
    checker = CodeQualityChecker()
    return checker.analyze_student_code(student_id, chapter, task_file)

if __name__ == "__main__":
    # 示例用法
    result = check_student_code("carrie", "c01", "MemoryVisibilityDemo.java")
    print(json.dumps(result, ensure_ascii=False, indent=2))