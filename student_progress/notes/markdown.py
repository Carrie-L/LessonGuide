#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
# Markdown格式化工具

## 功能特性

✅ **清理列表项之间的多余空行** - 让列表项紧密排列  
✅ **智能处理emoji** - 有序列表中删除emoji，其他地方替换为🌸  
✅ **保留重要符号** - ✅❌对错符号不受影响  
✅ **清理行尾空格** - 移除不必要的尾随空格  
✅ **处理空格行** - 将只包含空格的行转为真正空行  
✅ **优化段落间距** - 将多行空行缩减为一行，保持紧凑但有呼吸感  
✅ **移除分割线** - 删除所有markdown分割线（--- 或 ***），自动处理后续空行  

## 使用方法

```bash
# 直接覆盖原文件（推荐）
python markdown_formatter_final.py your_article.md

# 生成新文件（可选）
python markdown_formatter_final.py your_article.md formatted_article.md
```
"""

import re
import sys
import os


class MarkdownFormatter:
    """Markdown格式化器类"""

    def __init__(self):
        # 扩展的表情符号正则，包含更多Unicode范围（包括⚡等符号）
        self.emoji_pattern = re.compile(
            r'[\U0001F600-\U0001F64F]|[\U0001F300-\U0001F5FF]|[\U0001F680-\U0001F6FF]|'
            r'[\U0001F1E0-\U0001F1FF]|[\U00002700-\U000027BF]|[\U0001f926-\U0001f937]|'
            r'[\U00010000-\U0010ffff]|\u200d|\u2640-\u2642|\u2600-\u2B55|\u23cf|'
            r'\u23e9|\u231a|\ufe0f|\u3030|[\u2600-\u26FF]|[\u2700-\u27BF]'
        )

    def clean_emojis(self, text: str) -> str:
        """处理表情符号：有序列表中删除emoji，其他地方替换为🌸，保留✅❌"""
        # 先保护对错符号，用特殊标记替换
        text = text.replace('✅', '___CHECK_MARK___')
        text = text.replace('❌', '___CROSS_MARK___')
        
        # 处理有序列表中的emoji：删除emoji及其后面的空格
        lines = text.split('\n')
        processed_lines = []
        
        for line in lines:
            # 检查是否是有序列表项
            if re.match(r'^\d+\.\s+', line):
                # 在有序列表中，删除emoji及其后面的空格
                # 先删除emoji，然后清理多余空格
                line = self.emoji_pattern.sub('', line)
                # 清理emoji删除后可能留下的多余空格
                line = re.sub(r'\s+', ' ', line)  # 将多个空格合并为一个
            else:
                # 在其他地方，替换emoji为🌸
                line = self.emoji_pattern.sub('🌸', line)
            
            processed_lines.append(line)
        
        text = '\n'.join(processed_lines)
        
        # 恢复对错符号
        text = text.replace('___CHECK_MARK___', '✅')
        text = text.replace('___CROSS_MARK___', '❌')
        
        return text

    def clean_list_spacing_only(self, text: str) -> str:
        """只清理列表项之间的多余空行，其他格式完全不变"""
        
        # 第1步：将只包含空格/制表符的行替换为空行
        text = re.sub(r'^[ \t]+$', '', text, flags=re.MULTILINE)
        
        # 第2步：清理无序列表项之间的连续空行
        # 使用简单的替换，重复执行直到没有更多匹配
        prev_text = ""
        while prev_text != text:
            prev_text = text
            # 匹配：列表项 + 一个或多个空行 + 另一个列表项
            text = re.sub(r'(^[-*+]\s+.*?)\n(\n+)(^[-*+]\s+)', r'\1\n\3', text, flags=re.MULTILINE)
        
        # 第3步：清理有序列表项之间的连续空行
        prev_text = ""
        while prev_text != text:
            prev_text = text
            text = re.sub(r'(^\d+\.\s+.*?)\n(\n+)(^\d+\.\s+)', r'\1\n\3', text, flags=re.MULTILINE)
        
        return text

    def clean_trailing_spaces(self, text: str) -> str:
        """清理行尾空格"""
        return re.sub(r'[ \t]+$', '', text, flags=re.MULTILINE)

    def remove_dividers(self, text: str) -> str:
        """移除markdown分割线（--- 或 ***）"""
        # 移除独占一行的分割线
        # 匹配：行首可能的空白 + 三个或更多的 - 或 * + 行尾可能的空白
        text = re.sub(r'^\s*[-*]{3,}\s*$', '', text, flags=re.MULTILINE)
        return text

    def clean_excessive_blank_lines(self, text: str) -> str:
        """将多行空行缩减为一行空行，保持内容分隔但更紧凑"""
        # 将连续的多个空行（2行或以上）替换为单个空行
        text = re.sub(r'\n\n\n+', '\n\n', text)
        return text

    def format_markdown(self, text: str) -> str:
        """主格式化函数"""
        print("开始格式化Markdown文档...")

        # 1. 清理表情符号
        print("清理表情符号...")
        text = self.clean_emojis(text)

        # 2. 清理行尾空格
        print("清理行尾空格...")
        text = self.clean_trailing_spaces(text)

        # 3. 移除分割线
        print("移除分割线...")
        text = self.remove_dividers(text)

        # 4. 清理列表项之间的空行
        print("清理列表项间距...")
        text = self.clean_list_spacing_only(text)

        # 5. 清理多余的连续空行（包括移除分割线后可能产生的多余空行）
        print("优化段落间距...")
        text = self.clean_excessive_blank_lines(text)

        # 6. 确保文件结尾格式正确
        text = text.strip()
        if text:
            text += '\n'

        print("格式化完成！")
        return text

    def format_file(self, input_file: str, output_file: str = None) -> bool:
        """格式化单个文件"""
        try:
            print(f"读取文件: {input_file}")
            with open(input_file, 'r', encoding='utf-8') as f:
                content = f.read()

            formatted_content = self.format_markdown(content)

            if output_file is None:
                output_file = input_file
                print(f"覆盖原文件: {output_file}")
            else:
                print(f"写入新文件: {output_file}")

            with open(output_file, 'w', encoding='utf-8') as f:
                f.write(formatted_content)

            print(f"成功处理文件: {output_file}")
            return True

        except Exception as e:
            print(f"处理文件时出错: {e}")
            return False


def main():
    """主函数"""
    if len(sys.argv) < 2:
        print("使用方法: python markdown_formatter_final.py <输入文件> [输出文件]")
        print("示例:")
        print("  python markdown_formatter_final.py article.md              # 直接覆盖原文件")
        print("  python markdown_formatter_final.py article.md new.md      # 生成新文件")
        sys.exit(1)

    input_file = sys.argv[1]
    output_file = sys.argv[2] if len(sys.argv) > 2 else None

    if not os.path.exists(input_file):
        print(f"错误: 输入文件不存在: {input_file}")
        sys.exit(1)

    formatter = MarkdownFormatter()
    success = formatter.format_file(input_file, output_file)

    if success:
        if output_file:
            print(f"格式化完成！已生成新文件: {output_file}")
        else:
            print(f"格式化完成！已直接更新原文件: {input_file}")
    else:
        print("格式化失败！")
        sys.exit(1)


if __name__ == "__main__":
    main()
