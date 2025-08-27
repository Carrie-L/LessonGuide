#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
生成 roadmap v3.md 脚本
从 micro_tasks 目录读取指定顺序的文档，提取Task并生成新的roadmap文档
"""

import os
import re
from typing import List, Dict, Tuple

def extract_tasks_from_file(file_path: str) -> List[Tuple[str, str]]:
    """
    从单个文件中提取所有Task
    返回格式: [(task_id, title), ...]
    """
    tasks = []
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()

        # 使用正则表达式匹配Task行
        # 匹配格式: #### Task x.y.z: 标题 (时间) ⏰ [级别]
        # 也匹配: - [ ] **Task x.y.z: 标题 (时间) ⏰
        task_pattern = r'^#{1,4}\s*Task\s+(\d+\.\d+\.\d+):\s*(.+?)\s*\(?(\d+分钟)?\)?\s*⏰?\s*(\[.*?\])?\s*$'
        checkbox_pattern = r'^-\s*\[\s*\]\s*\*\*Task\s+(\d+\.\d+\.\d+):\s*(.+?)\s*\(?(\d+分钟)?\)?\s*⏰?\*\*\s*$'

        lines = content.split('\n')

        for line in lines:
            # 尝试匹配普通Task格式
            match = re.match(task_pattern, line.strip())
            if match:
                task_id = match.group(1)
                title = match.group(2).strip()
                # 清理标题，移除可能的时间和级别标记
                title = re.sub(r'\s*\(\d+分钟\)\s*⏰?\s*(\[.*?\])?\s*$', '', title).strip()
                tasks.append((task_id, title))
                continue

            # 尝试匹配复选框格式
            match = re.match(checkbox_pattern, line.strip())
            if match:
                task_id = match.group(1)
                title = match.group(2).strip()
                # 清理标题，移除可能的时间标记
                title = re.sub(r'\s*\(\d+分钟\)\s*⏰?\s*$', '', title).strip()
                tasks.append((task_id, title))

    except Exception as e:
        print(f"读取文件 {file_path} 时出错: {e}")

    return tasks

def get_chapter_file_mapping() -> Dict[str, str]:
    """
    获取章节号到文件名的映射
    """
    return {
        '1': 'MICRO_TASK_C01.md',
        '2': 'MICRO_TASK_C02.md',
        '3': 'MICRO_TASK_C03.md',
        '4': 'MICRO_TASK_C04.md',
        '5': 'MICRO_TASK_C05.md',
        '6': 'MICRO_TASK_C06.md',
        '7': 'MICRO_TASK_C07.md',
        '8': 'MICRO_TASK_C08.md',
        '9': 'MICRO_TASK_C09.md',
        '10': 'MICRO_TASK_C10.md',
        '11': 'MICRO_TASK_C11.md',
        '12': 'MICRO_TASK_C12.md'
    }

def generate_roadmap_v3(micro_tasks_dir: str, output_file: str, task_order: List[str]):
    """
    生成 roadmap v3.md 文件
    """
    print(f"开始生成 roadmap v3.md...")
    print(f"micro_tasks 目录: {micro_tasks_dir}")
    print(f"输出文件: {output_file}")

    # 获取章节映射
    chapter_mapping = get_chapter_file_mapping()

    # 存储所有提取的tasks，按章节分组
    all_tasks = {}  # chapter -> {subchapter: [tasks]}

    # 按照用户指定的顺序处理
    for chapter_sub in task_order:
        chapter = chapter_sub.split('.')[0]
        subchapter = chapter_sub

        if chapter not in chapter_mapping:
            print(f"警告: 章节 {chapter} 没有对应的文件映射")
            continue

        file_name = chapter_mapping[chapter]
        file_path = os.path.join(micro_tasks_dir, file_name)

        if not os.path.exists(file_path):
            print(f"警告: 文件 {file_path} 不存在")
            continue

        print(f"正在处理: {chapter_sub} -> {file_name}")

        # 提取该文件的所有tasks
        if chapter not in all_tasks:
            all_tasks[chapter] = {}

        if subchapter not in all_tasks[chapter]:
            tasks = extract_tasks_from_file(file_path)
            # 过滤出属于当前子章节的tasks
            subchapter_tasks = [task for task in tasks if task[0].startswith(subchapter + '.')]
            all_tasks[chapter][subchapter] = subchapter_tasks
            print(f"  找到 {len(subchapter_tasks)} 个任务")

    # 生成输出文件
    with open(output_file, 'w', encoding='utf-8') as f:
        f.write("# Android 开发学习路线图 V3\n\n")
        f.write("按照指定顺序组织的学习任务清单\n\n")

        current_chapter = None

        # 按照用户指定的顺序输出
        for chapter_sub in task_order:
            chapter = chapter_sub.split('.')[0]

            if chapter != current_chapter:
                current_chapter = chapter
                f.write(f"## Chapter {chapter}\n\n")

            if chapter in all_tasks and chapter_sub in all_tasks[chapter]:
                tasks = all_tasks[chapter][chapter_sub]
                if tasks:
                    f.write(f"### {chapter_sub}\n\n")
                    for task_id, title in tasks:
                        f.write(f"- [ ] Task {task_id}: {title}\n")
                    f.write("\n")
                else:
                    f.write(f"### {chapter_sub}\n\n")
                    f.write("(暂无任务)\n\n")
            else:
                f.write(f"### {chapter_sub}\n\n")
                f.write("(未找到对应任务)\n\n")

    print(f"roadmap v3.md 生成完成!")

def main():
    # 配置路径
    micro_tasks_dir = "micro_tasks"
    output_file = "roadmap v3.md"

    # 用户指定的任务顺序
    task_order = [
        "2.2", "2.3", "3.1", "3.2", "3.3", "3.4", "5.1", "5.2",
        "8.1", "8.2", "8.3", "8.4", "4.1", "4.2", "4.3", "2.1",
        "9.2", "9.3", "9.4", "9.5", "10.1", "10.2", "10.3", "10.4",
        "9.1", "12.1", "12.2", "12.3", "11.1", "11.2", "11.3", "11.4",
        "6.1", "6.2", "6.3", "6.4"
    ]

    print(f"当前工作目录: {os.getcwd()}")
    print(f"micro_tasks_dir 绝对路径: {os.path.abspath(micro_tasks_dir)}")

    # 检查micro_tasks目录是否存在
    if not os.path.exists(micro_tasks_dir):
        print(f"错误: micro_tasks 目录 '{micro_tasks_dir}' 不存在!")
        print(f"当前目录下的文件: {os.listdir('.')}")
        return

    # 生成roadmap
    generate_roadmap_v3(micro_tasks_dir, output_file, task_order)

    # 显示统计信息
    print("\n生成统计:")
    print(f"- 处理的任务章节数: {len(task_order)}")
    print(f"- 输出文件: {output_file}")

if __name__ == "__main__":
    main()
