#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Windows桌面时钟 + TodoList应用
- 固定在所有窗口最上层
- 超大时钟显示
- 右侧TodoList功能
- 可拖拽移动位置
- 半透明背景
- 任务持久化保存
"""

import tkinter as tk
from tkinter import ttk
from datetime import datetime, timedelta
import sys
import json
import os
import webbrowser
import uuid
from collections import defaultdict
try:
    from PIL import Image, ImageTk
    PIL_AVAILABLE = True
except ImportError:
    PIL_AVAILABLE = False
    print("PIL not available, cyberpunk background will use solid color")

class DesktopClock:
    def __init__(self):
        self.root = tk.Tk()
        self.todos = []
        self.todo_file = "desktop_todos.json"
        self.current_theme = "pink"  # 默认粉色主题
        self.load_todos()
        self.setup_window()
        self.create_widgets()
        # 在所有组件创建后，应用初始主题
        self.apply_theme(self.current_theme)
        self.make_draggable()
        self.update_time()
        
    def setup_window(self):
        """设置窗口属性"""
        # 移除窗口装饰（标题栏、边框）
        self.root.overrideredirect(True)
        
        # 设置窗口始终在最上层
        self.root.attributes('-topmost', True)
        
        # 设置半透明背景
        self.root.attributes('-alpha', 1)
        
        # 设置背景颜色或背景图片
        if self.current_theme == "cyberpunk":
            self.setup_cyberpunk_background()
        else:
            self.root.configure(bg='#FFFFFF')
        
        # 设置窗口大小和位置（右上角）
        window_width = 1200  # 左侧800 + 右侧400的总宽度
        window_height = 500  # 固定高度
        screen_width = self.root.winfo_screenwidth()
        screen_height = self.root.winfo_screenheight()
        x = screen_width - window_width - 20  # 距离右边20像素
        y = 20  # 距离顶部20像素
        
        self.root.geometry(f'{window_width}x{window_height}+{x}+{y}')
        
        # 确保背景不透明
        try:
            # 移除透明色设置，确保白色背景可见
            pass
        except:
            pass
    
    def setup_background_image(self, image_path):
        """在主Canvas上设置背景图片"""
        if not PIL_AVAILABLE:
            print("PIL/Pillow not installed. Cannot display background image.")
            return

        if not os.path.exists(image_path):
            print(f"Background image not found: {image_path}")
            return

        try:
            # 移除旧的背景图
            self.remove_background_image()

            # 加载并调整图片大小为窗口大小
            img = Image.open(image_path).resize((1200, 500), Image.Resampling.LANCZOS)
            self.bg_photo = ImageTk.PhotoImage(img)

            # 在主Canvas上绘制背景图片（如果main_canvas已创建）
            if hasattr(self, 'main_canvas'):
                # 清除Canvas上的旧背景（如果有）
                if hasattr(self, '_bg_image_id'):
                    self.main_canvas.delete(self._bg_image_id)
                
                # 在Canvas上绘制新背景图片
                self._bg_image_id = self.main_canvas.create_image(0, 0, image=self.bg_photo, anchor="nw")
                
                # 确保背景图在最底层
                self.main_canvas.tag_lower(self._bg_image_id)
            
            print(f"🎨 背景图片 {image_path} 加载成功！")

        except Exception as e:
            print(f"Failed to load background image: {e}")
            self.bg_photo = None

    def remove_background_image(self):
        """移除背景图片"""
        try:
            # 移除Canvas上的背景图片元素
            if hasattr(self, '_bg_image_id') and hasattr(self, 'main_canvas'):
                self.main_canvas.delete(self._bg_image_id)
                delattr(self, '_bg_image_id')
        except:
            pass
        try:
            if hasattr(self, '_bg_canvas'):
                if hasattr(self._bg_canvas, 'winfo_exists') and self._bg_canvas.winfo_exists():
                    self._bg_canvas.destroy()
                delattr(self, '_bg_canvas')
        except:
            pass
        try:
            if hasattr(self, '_bg_label'):
                if hasattr(self._bg_label, 'winfo_exists') and self._bg_label.winfo_exists():
                    self._bg_label.destroy()
                delattr(self, '_bg_label')
        except:
            pass
        try:
            if hasattr(self, 'bg_photo'):
                delattr(self, 'bg_photo')
        except:
            pass
    
    def create_widgets(self):
        """创建时钟显示组件和TodoList - 使用Canvas布局"""
        # 创建主Canvas，用于背景和组件布局
        self.main_canvas = tk.Canvas(self.root, width=1200, height=500, highlightthickness=0, bd=0)
        self.main_canvas.pack(fill="both", expand=True)
        
        # 创建时钟区域Frame（作为Canvas上的窗口）
        self.clock_frame = tk.Frame(self.main_canvas, width=800, height=500)
        
        # 创建一个内部容器来实现垂直居中
        clock_container = tk.Frame(self.clock_frame)
        clock_container.place(relx=0.5, rely=0.5, anchor="center")
        
        # 时间显示标签 - 完全居中
        self.time_label = tk.Label(
            clock_container,
            text="00:00:00",
            font=("Consolas", 135, "bold"),
            anchor='center'
        )
        self.time_label.pack()
        
        # 日期显示标签 - 完全居中
        self.date_label = tk.Label(
            clock_container,
            text="2024-01-01 周一",
            font=("Microsoft YaHei UI", 18, "bold"),
            anchor='center'
        )
        self.date_label.pack(pady=(10, 0))
        

        
        # 将时钟Frame放置在Canvas上
        self.main_canvas.create_window(15, 0, anchor="nw", window=self.clock_frame)
        
        # 创建分隔线Frame
        separator = tk.Frame(self.main_canvas, width=3, height=500)
        self.main_canvas.create_window(820, 0, anchor="nw", window=separator)
        
        # 右侧TodoList区域Frame - 进一步减少宽度，消除右侧空白
        self.todo_main_frame = tk.Frame(self.main_canvas, width=350, height=500, bd=0, highlightthickness=0)
        self.todo_main_frame.pack_propagate(False)  # 防止子组件改变Frame大小
        self.main_canvas.create_window(845, 0, anchor="nw", window=self.todo_main_frame)
        
        # TodoList区域
        self.create_todolist()
        
        # 主题切换按钮 - 放在窗口右下角
        self.create_theme_buttons()
        
        # 右键菜单 - 保留其他功能，移除切换主题
        self.create_context_menu()
    
    def create_theme_buttons(self):
        """在时钟窗口右下角创建主题切换按钮"""
        # 4个主题按钮
        themes = [
            ("🌸", "pink", "粉色主题"),
            ("🌿", "black", "绿色主题"), 
            ("🌙", "white", "黑色主题"),
            ("🔮", "cyberpunk", "赛博朋克")
        ]
        
        self.theme_buttons = []
        for i, (emoji, theme_name, tooltip) in enumerate(themes):
            btn = tk.Button(
                self.clock_frame,
                text=emoji,
                font=("Microsoft YaHei UI", 16),
                relief="flat",
                fg="#bda6ff",
                bd=0,
                highlightthickness=0,
                cursor="hand2",
                command=lambda t=theme_name: self.apply_theme(t)
            )
            # 放在时钟窗口右下角
            btn.place(x=670 + i * 30, y=450)  # 时钟窗口内的右下角位置
            self.theme_buttons.append(btn)
    
    def create_todolist(self):
        """创建TodoList组件"""
        # TodoList标题（颜色将由apply_theme控制）
        self.todo_title = tk.Label(
            self.todo_main_frame,
            text="📋 今日任务",
            font=("Microsoft YaHei UI", 16, "bold")
        )
        self.todo_title.pack(anchor='w', pady=(0, 15), padx=(0, 5))
        
        # 输入区域（背景色将由apply_theme控制）
        self.input_frame = tk.Frame(self.todo_main_frame, bd=0, highlightthickness=0)
        self.input_frame.pack(fill='x', pady=(0, 15), padx=(0, 5))  # 添加左右内边距防止超出
        
        # 输入框（颜色将由apply_theme控制）
        self.todo_entry = tk.Entry(
            self.input_frame,
            font=("Microsoft YaHei UI", 9),
            relief='flat',
            bd=4
        )
        self.todo_entry.pack(side='left', fill='x', expand=True, padx=(0, 4))
        self.todo_entry.bind('<Return>', self.add_todo)
        
        # 添加按钮（颜色将由apply_theme控制）
        self.add_btn = tk.Button(
            self.input_frame,
            text="➕",
            font=("Microsoft YaHei UI", 9),
            relief='flat',
            command=self.add_todo,
            width=3,
            height=1,
            bd=0,
            highlightthickness=0
        )
        self.add_btn.pack(side='right', padx=(1, 0))
        
        # 任务列表区域（背景色将由apply_theme控制）
        self.todo_list_frame = tk.Frame(self.todo_main_frame)
        self.todo_list_frame.pack(fill='both', expand=True)
        
        # 滚动条（背景色将由apply_theme控制）
        self.canvas = tk.Canvas(self.todo_list_frame, highlightthickness=0)
        self.scrollbar = ttk.Scrollbar(self.todo_list_frame, orient="vertical", command=self.canvas.yview)
        self.scrollable_frame = tk.Frame(self.canvas)
        
        self.scrollable_frame.bind(
            "<Configure>",
            lambda e: self.canvas.configure(scrollregion=self.canvas.bbox("all"))
        )
        
        # 让scrollable_frame的宽度跟随canvas的宽度
        def configure_scrollable_frame(event):
            canvas_width = event.width
            self.canvas.itemconfig(self.canvas_window, width=canvas_width)
        
        self.canvas.bind('<Configure>', configure_scrollable_frame)
        
        self.canvas_window = self.canvas.create_window((0, 0), window=self.scrollable_frame, anchor="nw")
        self.canvas.configure(yscrollcommand=self.scrollbar.set)
        
        # 绑定鼠标滚轮事件
        self.bind_mousewheel()
        
        self.canvas.pack(side="left", fill="both", expand=True)
        self.scrollbar.pack(side="right", fill="y")
        
        # 刷新任务显示
        self.refresh_todos()
    
    def bind_mousewheel(self):
        """绑定鼠标滚轮事件"""
        def on_mousewheel(event):
            # Windows系统下的滚轮事件处理
            self.canvas.yview_scroll(int(-1 * (event.delta / 120)), "units")
        
        def on_mousewheel_linux(event):
            # Linux系统下的滚轮事件处理
            if event.num == 4:
                self.canvas.yview_scroll(-1, "units")
            elif event.num == 5:
                self.canvas.yview_scroll(1, "units")
        
        # 绑定到canvas和相关组件
        self.canvas.bind("<MouseWheel>", on_mousewheel)  # Windows
        self.canvas.bind("<Button-4>", on_mousewheel_linux)  # Linux
        self.canvas.bind("<Button-5>", on_mousewheel_linux)  # Linux
        
        # 让canvas能够接收焦点以响应滚轮事件
        self.canvas.focus_set()
        
        # 当鼠标进入任务区域时，让canvas获得焦点
        def on_enter(event):
            self.canvas.focus_set()
        
        def on_leave(event):
            self.root.focus_set()
        
        self.canvas.bind("<Enter>", on_enter)
        self.canvas.bind("<Leave>", on_leave)
        
        # 同时绑定到整个todo区域
        self.todo_list_frame.bind("<MouseWheel>", on_mousewheel)
        self.todo_list_frame.bind("<Button-4>", on_mousewheel_linux)
        self.todo_list_frame.bind("<Button-5>", on_mousewheel_linux)
        
        # 添加长按拖拽滚动功能
        self.setup_drag_scroll()
    
    def setup_drag_scroll(self):
        """设置长按拖拽滚动功能"""
        self.drag_data = {"y": 0, "scrolling": False, "start_time": 0}
        
        def on_button_press(event):
            """鼠标按下事件"""
            import time
            self.drag_data["y"] = event.y
            self.drag_data["scrolling"] = False
            self.drag_data["start_time"] = time.time()
            # 设置一个定时器，如果长按超过200ms就开始拖拽模式
            self.root.after(200, lambda: self.check_long_press(event))
        
        def on_button_release(event):
            """鼠标释放事件"""
            self.drag_data["scrolling"] = False
        
        def on_mouse_drag(event):
            """鼠标拖拽事件"""
            if self.drag_data["scrolling"]:
                # 计算垂直移动距离
                delta_y = self.drag_data["y"] - event.y
                # 滚动速度调整（可以调节这个值来改变滚动灵敏度）
                scroll_speed = max(1, abs(delta_y) // 10)
                
                if delta_y > 5:  # 向上拖拽，列表向下滚动
                    for _ in range(scroll_speed):
                        self.canvas.yview_scroll(1, "units")
                elif delta_y < -5:  # 向下拖拽，列表向上滚动
                    for _ in range(scroll_speed):
                        self.canvas.yview_scroll(-1, "units")
                
                self.drag_data["y"] = event.y
        
        # 绑定到canvas和scrollable_frame
        self.canvas.bind("<Button-1>", on_button_press)
        self.canvas.bind("<ButtonRelease-1>", on_button_release)
        self.canvas.bind("<B1-Motion>", on_mouse_drag)
        
        self.scrollable_frame.bind("<Button-1>", on_button_press)
        self.scrollable_frame.bind("<ButtonRelease-1>", on_button_release)
        self.scrollable_frame.bind("<B1-Motion>", on_mouse_drag)
    
    def check_long_press(self, event):
        """检查是否为长按操作"""
        import time
        if time.time() - self.drag_data["start_time"] >= 0.2:  # 长按200ms
            self.drag_data["scrolling"] = True
    
    def load_todos(self):
        """从文件加载TodoList"""
        try:
            if os.path.exists(self.todo_file):
                with open(self.todo_file, 'r', encoding='utf-8') as f:
                    self.todos = json.load(f)
        except Exception as e:
            print(f"加载任务列表失败: {e}")
            self.todos = []
    
    def save_todos(self):
        """保存TodoList到文件"""
        try:
            with open(self.todo_file, 'w', encoding='utf-8') as f:
                json.dump(self.todos, f, ensure_ascii=False, indent=2)
        except Exception as e:
            print(f"保存任务列表失败: {e}")
    
    def add_todo(self, event=None):
        """添加新任务"""
        task_text = self.todo_entry.get().strip()
        if task_text:
            # 生成唯一ID
            import time
            todo_id = int(time.time() * 1000)  # 使用时间戳确保唯一性
            
            todo_item = {
                'id': todo_id,
                'text': task_text,
                'completed': False,
                'created_at': datetime.now().strftime('%Y-%m-%d %H:%M:%S'),
                'updated_at': datetime.now().strftime('%Y-%m-%d %H:%M:%S')
            }
            self.todos.append(todo_item)
            self.todo_entry.delete(0, tk.END)
            self.save_todos()
            self.refresh_todos()
    
    def toggle_todo(self, todo_id):
        """切换任务完成状态"""
        for todo in self.todos:
            if todo['id'] == todo_id:
                todo['completed'] = not todo['completed']
                todo['updated_at'] = datetime.now().strftime('%Y-%m-%d %H:%M:%S')
                # 添加完成时间戳
                if todo['completed']:
                    todo['completed_at'] = datetime.now().strftime('%Y-%m-%d %H:%M:%S')
                else:
                    todo.pop('completed_at', None)  # 移除完成时间
                break
        self.save_todos()
        self.refresh_todos()
    
    def delete_todo(self, todo_id):
        """删除任务"""
        self.todos = [todo for todo in self.todos if todo['id'] != todo_id]
        self.save_todos()
        self.refresh_todos()
    
    def refresh_todos(self, hide_completed=False):
        """刷新任务显示"""
        # 清除现有任务显示
        for widget in self.scrollable_frame.winfo_children():
            widget.destroy()
        
        # 根据参数决定显示哪些任务
        if hide_completed:
            todos_to_show = [todo for todo in self.todos if not todo.get('completed', False)]
        else:
            todos_to_show = self.todos
        
        # 显示任务列表
        for i, todo in enumerate(todos_to_show):
            self.create_todo_item(todo, i)
    
    def create_todo_item(self, todo, index):
        """创建单个任务项 - 美观优化版本"""
        colors = self.get_theme_colors(self.current_theme)
        
        # 优化的任务项Frame - 更美观的边框和间距
        item_frame = tk.Frame(
            self.scrollable_frame, 
            bg=colors["item_bg"], 

            highlightbackground=colors["separator"],
            highlightcolor=colors["separator"],
            highlightthickness=0
        )
        item_frame.pack(fill='both', padx=0, pady=3)  # 进一步减少垂直间距
        
        # 内部容器Frame，提供合适的内边距
        inner_frame = tk.Frame(item_frame, bg=colors["item_bg"])
        inner_frame.pack(fill='both', expand=True, padx=3, pady=0)  # 进一步减少内边距
        
        # 完成状态按钮 - 更精致的设计
        check_text = "✅" if todo["completed"] else "⚪"
        check_bg = colors["check_completed_bg"] if todo["completed"] else colors["check_bg"]
        check_fg = colors["check_completed_fg"] if todo["completed"] else colors["check_fg"]

        check_btn = tk.Button(
            inner_frame, 
            text=check_text, 
            font=("Microsoft YaHei UI", 16),  # 调整字体大小
            bg=colors["item_bg"],
            fg=check_fg,
            relief="flat", 
            bd=0, 
            highlightthickness=0,
            activebackground=colors["item_bg"],
            cursor="hand2",  # 手型光标
            width=2,  # 减小按钮宽度
            command=lambda tid=todo["id"]: self.toggle_todo(tid)
        )
        check_btn.pack(side='left', padx=(0, 8))  # 进一步减少左边距
        
        # 任务文本 - 更好的排版
        text_color = colors["task_completed_fg"] if todo['completed'] else colors["task_fg"]
        text_font = ("Microsoft YaHei UI", 13, 'overstrike' if todo['completed'] else 'normal')
        
        task_label = tk.Label(
            inner_frame,
            text=todo['text'],
            font=text_font,
            fg=text_color,
            bg=colors["item_bg"],
            anchor='w',
            wraplength=320,  # 适应新的TodoList宽度
            justify='left'
        )
        task_label.pack(side='left', fill='both', expand=True, padx=(0, 0))  # 进一步减少右边距
        
        # 删除按钮 - 更精致的设计
        delete_btn = tk.Button(
            inner_frame,
            text="🗑️",
            font=("Microsoft YaHei UI", 14),  # 调整字体大小
            bg=colors["item_bg"],
            fg=colors["delete_fg"],
            relief='flat',
            command=lambda: self.delete_todo(todo['id']),
            width=4,  # 减小按钮宽度
            bd=0,
            highlightthickness=0,
            activebackground=colors["item_bg"],
            cursor="hand2"  # 手型光标
        )
        delete_btn.pack(side='right', padx=(0, 0))  # 删除按钮紧贴右边
        
        # 为任务项的所有组件绑定鼠标滚轮事件
        self.bind_mousewheel_to_widget(item_frame)
        self.bind_mousewheel_to_widget(inner_frame)
        # 只为非按钮区域绑定长按拖拽
        self.bind_mousewheel_only(check_btn)  # 按钮只要滚轮，不要拖拽
        self.bind_mousewheel_to_widget(task_label)
        self.bind_mousewheel_only(delete_btn)  # 按钮只要滚轮，不要拖拽
        
        # 为任务项添加右键复制功能
        self.bind_task_context_menu(item_frame, todo['text'])
        self.bind_task_context_menu(inner_frame, todo['text'])
        self.bind_task_context_menu(task_label, todo['text'])
    
    def bind_task_context_menu(self, widget, task_text):
        """为任务项组件绑定右键复制菜单"""
        def show_task_context_menu(event):
            # 创建右键菜单 - 统一橙棕色样式
            task_menu = tk.Menu(self.root, tearoff=0,
                               bg="#d87a57", 
                               fg="#FFFFFF",
                               selectcolor="#FFFFFF", 
                               activebackground="#e89370",
                               activeforeground="#FFFFFF",
                               disabledforeground="#CCCCCC")
            task_menu.add_command(
                label="📋 复制任务内容", 
                command=lambda: self.copy_task_text_and_close_menu(task_text, task_menu)
            )
            
            try:
                task_menu.tk_popup(event.x_root, event.y_root)
            finally:
                task_menu.grab_release()
        
        widget.bind("<Button-3>", show_task_context_menu)
    
    def copy_task_text_and_close_menu(self, text, menu):
        """复制任务文本并关闭菜单"""
        self.copy_task_text(text)
        try:
            menu.unpost()
        except:
            pass
    
    def copy_task_text(self, text):
        """复制任务文本到剪贴板"""
        try:
            self.root.clipboard_clear()
            self.root.clipboard_append(text)
            self.root.update()  # 确保剪贴板更新
            print(f"已复制到剪贴板: {text}")
        except Exception as e:
            print(f"复制失败: {e}")
    
    def bind_mousewheel_to_widget(self, widget):
        """为指定组件绑定鼠标滚轮事件和长按拖拽"""
        def on_mousewheel(event):
            self.canvas.yview_scroll(int(-1 * (event.delta / 120)), "units")
        
        def on_mousewheel_linux(event):
            if event.num == 4:
                self.canvas.yview_scroll(-1, "units")
            elif event.num == 5:
                self.canvas.yview_scroll(1, "units")
        
        def on_button_press(event):
            """鼠标按下事件"""
            import time
            self.drag_data["y"] = event.y_root
            self.drag_data["scrolling"] = False
            self.drag_data["start_time"] = time.time()
            self.root.after(200, lambda: self.check_long_press(event))
        
        def on_button_release(event):
            """鼠标释放事件"""
            self.drag_data["scrolling"] = False
        
        def on_mouse_drag(event):
            """鼠标拖拽事件"""
            if self.drag_data["scrolling"]:
                delta_y = self.drag_data["y"] - event.y_root
                scroll_speed = max(1, abs(delta_y) // 10)
                
                if delta_y > 5:
                    for _ in range(scroll_speed):
                        self.canvas.yview_scroll(1, "units")
                elif delta_y < -5:
                    for _ in range(scroll_speed):
                        self.canvas.yview_scroll(-1, "units")
                
                self.drag_data["y"] = event.y_root
        
        widget.bind("<MouseWheel>", on_mousewheel)  # Windows
        widget.bind("<Button-4>", on_mousewheel_linux)  # Linux
        widget.bind("<Button-5>", on_mousewheel_linux)  # Linux
        widget.bind("<Button-1>", on_button_press)
        widget.bind("<ButtonRelease-1>", on_button_release)
        widget.bind("<B1-Motion>", on_mouse_drag)

    def bind_mousewheel_only(self, widget):
        """仅为指定组件绑定鼠标滚轮事件（不包括长按拖拽）"""
        def on_mousewheel(event):
            self.canvas.yview_scroll(int(-1 * (event.delta / 120)), "units")
        
        def on_mousewheel_linux(event):
            if event.num == 4:
                self.canvas.yview_scroll(-1, "units")
            elif event.num == 5:
                self.canvas.yview_scroll(1, "units")
        
        widget.bind("<MouseWheel>", on_mousewheel)  # Windows
        widget.bind("<Button-4>", on_mousewheel_linux)  # Linux
        widget.bind("<Button-5>", on_mousewheel_linux)  # Linux

    def create_context_menu(self):
        """创建右键菜单"""
        self.context_menu = tk.Menu(self.root, tearoff=0,
                                   bg="#d87a57", 
                                   fg="#FFFFFF",
                                   selectcolor="#FFFFFF", 
                                   activebackground="#e89370",
                                   activeforeground="#FFFFFF",
                                   disabledforeground="#CCCCCC")
        
        # 重新定义每个命令，确保都会关闭菜单
        def toggle_and_close():
            print("菜单：切换样式")
            # 多重强制关闭菜单
            try:
                self.context_menu.unpost()
                self.context_menu.grab_release()
                # 强制处理所有待处理事件，确保菜单关闭
                self.root.update_idletasks()
                self.root.update()
                # 再次确保菜单关闭
                self.context_menu.unpost()
            except:
                pass
            # 立即执行主题切换
            self.toggle_style()
            
        def stats_and_close():
            print("菜单：查看统计")
            try:
                self.context_menu.unpost()
                self.context_menu.grab_release()
                self.root.update_idletasks()
                self.context_menu.unpost()
            except:
                pass
            self.show_statistics()
            
        def clear_completed_and_close():
            print("菜单：清除已完成")
            try:
                self.context_menu.unpost()
                self.context_menu.grab_release()
                self.root.update_idletasks()
                self.context_menu.unpost()
            except:
                pass
            self.clear_completed()
            
        def clear_all_and_close():
            print("菜单：清除所有")
            try:
                self.context_menu.unpost()
                self.context_menu.grab_release()
                self.root.update_idletasks()
                self.context_menu.unpost()
            except:
                pass
            self.clear_all()
        

        self.context_menu.add_command(label="📊 查看统计", command=stats_and_close)
        self.context_menu.add_separator()
        self.context_menu.add_command(label="🙈 隐藏已完成任务", command=clear_completed_and_close)
        self.context_menu.add_command(label="👀 显示所有任务", command=lambda: self.show_all_and_close())
        self.context_menu.add_command(label="🗑️ 清除所有任务", command=clear_all_and_close)
        self.context_menu.add_separator()
        self.context_menu.add_command(label="退出", command=self.quit_app)
        
        # 绑定右键事件 - 保留其他功能
        self.root.bind("<Button-3>", self.show_context_menu)
        self.time_label.bind("<Button-3>", self.show_context_menu)
        self.date_label.bind("<Button-3>", self.show_context_menu)
    
    def clear_completed(self):
        """隐藏已完成的任务（数据保留用于统计）"""
        # 数据保持不变，只刷新显示（只显示未完成任务）
        self.refresh_todos(hide_completed=True)
    
    def show_all_and_close(self):
        """显示所有任务并关闭菜单"""
        try:
            self.context_menu.unpost()
            self.context_menu.grab_release()
        except:
            pass
        self.refresh_todos(hide_completed=False)
    
    def clear_all(self):
        """清除所有任务"""
        self.todos = []
        self.save_todos()
        self.refresh_todos()
    
    def show_context_menu(self, event):
        """显示右键菜单"""
        try:
            if self.root.winfo_exists():
                # 多重确保之前的菜单关闭
                try:
                    self.context_menu.unpost()
                    self.context_menu.grab_release()
                    # 强制处理事件
                    self.root.update_idletasks()
                    # 再次确保关闭
                    self.context_menu.unpost()
                except:
                    pass
                # 显示新菜单
                self.context_menu.tk_popup(event.x_root, event.y_root)
        except Exception as e:
            print(f"菜单显示错误: {e}")
        finally:
            try:
                if self.root.winfo_exists():
                    self.context_menu.grab_release()
            except:
                pass
    
    def get_theme_colors(self, theme):
        """获取主题配色（纯色版本，不使用背景图片）"""
        themes = {
            "pink": {  # 白底粉色
                "bg": "#FFFFFF",
                "time_fg": "#FF69B4",
                "date_fg": "#FF1493",
                "title_fg": "#FF1493",
                "entry_bg": "#FFF0F5",
                "entry_fg": "#FF1493",
                "btn_bg": "#FF69B4",
                "btn_fg": "#FFFFFF",
                "separator": "#d87a57",
                "item_bg": "#FFF0F5",
                "task_fg": "#FF1493",
                "task_completed_fg": "#FFB6C1",
                "check_fg": "#FF69B4",
                "check_completed_fg": "#FFB6C1",
                "delete_fg": "#FF69B4",
                "check_bg": "#FFF0F5",
                "check_completed_bg": "#FFE4E1"
            },
            "black": {  # 绿色清新主题
                "bg": "#edfeb6",
                "time_fg": "#aad785",
                "date_fg": "#aad785",
                "title_fg": "#aad785",
                "entry_bg": "#ddf0c0",
                "entry_fg": "#5a8a3a",
                "btn_bg": "#aad785",
                "btn_fg": "#FFFFFF",
                "separator": "#d87a57",
                "item_bg": "#ddf0c0",
                "task_fg": "#5a8a3a",
                "task_completed_fg": "#888888",
                "check_fg": "#aad785",
                "check_completed_fg": "#888888",
                "delete_fg": "#aad785",
                "check_bg": "#ddf0c0",
                "check_completed_bg": "#c8e2a8"
            },
            "white": {  # 黑底白色
                "bg": "#000000",
                "time_fg": "#FFFFFF",
                "date_fg": "#CCCCCC",
                "title_fg": "#FFFFFF",
                "entry_bg": "#2C2C2C",
                "entry_fg": "#FFFFFF",
                "btn_bg": "#666666",
                "btn_fg": "#FFFFFF",
                "separator": "#d87a57",
                "item_bg": "#2C2C2C",
                "task_fg": "#FFFFFF",
                "task_completed_fg": "#888888",
                "check_fg": "#FFFFFF",
                "check_completed_fg": "#CCCCCC",
                "delete_fg": "#CCCCCC",
                "check_bg": "#2C2C2C",
                "check_completed_bg": "#404040"
            },
            "cyberpunk": {  # 赛博朋克风格
                "bg": "#0A0A0F",
                "time_fg": "#00FFFF",
                "date_fg": "#FF0080",
                "title_fg": "#FF0080",
                "entry_bg": "#1A1A2E",
                "entry_fg": "#00FFFF",
                "btn_bg": "#FF0080",
                "btn_fg": "#000000",
                "separator": "#d87a57",
                "item_bg": "#16213E",
                "task_fg": "#00FFFF",
                "task_completed_fg": "#888888",
                "check_fg": "#00FF41",
                "check_completed_fg": "#000000",
                "delete_fg": "#FF0080",
                "check_bg": "#16213E",
                "check_completed_bg": "#1A3A1A"
            }
        }
        return themes.get(theme, themes["pink"])

    def apply_theme(self, theme):
        """应用指定的主题（纯色版本）"""
        self.current_theme = theme
        colors = self.get_theme_colors(theme)
        
        # 移除所有背景图片
        self.remove_background_image()
        
        # 设置窗口背景
        self.root.attributes('-alpha', 1.0)
        self.root.configure(bg=colors["bg"])
        
        # 更新所有组件样式 - 统一使用纯色背景
        if hasattr(self, 'main_canvas'):
            self.main_canvas.configure(bg=colors["bg"])
        
        if hasattr(self, 'clock_frame'):
            self.clock_frame.configure(bg=colors["bg"])
            # 配置时钟内部容器
            for widget in self.clock_frame.winfo_children():
                if isinstance(widget, tk.Frame):
                    widget.configure(bg=colors["bg"])
        
        if hasattr(self, 'time_label'):
            self.time_label.configure(fg=colors["time_fg"], bg=colors["bg"])
        
        if hasattr(self, 'date_label'):
            self.date_label.configure(fg=colors["date_fg"], bg=colors["bg"])
        
        # 配置主题按钮（时钟窗口右下角，完全无背景）
        if hasattr(self, 'theme_buttons'):
            for btn in self.theme_buttons:
                # 完全移除背景，只显示emoji
                try:
                    btn.configure(bg=colors["bg"], activebackground=colors["bg"], relief='flat')
                except:
                    pass

        # 分隔线
        try:
            children = self.root.winfo_children()
            if len(children) > 1:
                separator = children[1]
                separator.configure(bg=colors["separator"])
        except:
            pass

        # TodoList区域
        if hasattr(self, 'todo_main_frame'):
            self.todo_main_frame.configure(bg=colors["bg"])
        
        if hasattr(self, 'todo_title'):
            self.todo_title.configure(fg=colors["title_fg"], bg=colors["bg"])
        
        if hasattr(self, 'input_frame'):
            self.input_frame.configure(bg=colors["bg"])
        
        if hasattr(self, 'todo_entry'):
            self.todo_entry.configure(bg=colors["entry_bg"], fg=colors["entry_fg"], insertbackground=colors["entry_fg"])
        
        if hasattr(self, 'add_btn'):
            self.add_btn.configure(bg=colors["btn_bg"], fg=colors["btn_fg"], activebackground=colors["btn_bg"])
        
        if hasattr(self, 'todo_list_frame'):
            self.todo_list_frame.configure(bg=colors["bg"])
        
        if hasattr(self, 'canvas'):
            self.canvas.configure(bg=colors["bg"])
        
        if hasattr(self, 'scrollable_frame'):
            self.scrollable_frame.configure(bg=colors["bg"])
            # 更新所有任务项的内部Frame背景
            for widget in self.scrollable_frame.winfo_children():
                if isinstance(widget, tk.Frame):
                    widget.configure(bg=colors["item_bg"])
                    # 更新inner_frame
                    for inner_widget in widget.winfo_children():
                        if isinstance(inner_widget, tk.Frame):
                            inner_widget.configure(bg=colors["item_bg"])
        
        # 刷新任务列表
        self.refresh_todos()

    def toggle_style_and_close_menu(self):
        """切换样式并关闭菜单"""
        print("执行：切换样式并关闭菜单")
        self.close_context_menu()
        self.toggle_style()
    
    def show_statistics_and_close_menu(self):
        """查看统计并关闭菜单"""
        print("执行：查看统计并关闭菜单")
        self.close_context_menu()
        self.show_statistics()
    
    def clear_completed_and_close_menu(self):
        """清除已完成任务并关闭菜单"""
        print("执行：清除已完成任务并关闭菜单")
        self.close_context_menu()
        self.clear_completed()
    
    def clear_all_and_close_menu(self):
        """清除所有任务并关闭菜单"""
        print("执行：清除所有任务并关闭菜单")
        self.close_context_menu()
        self.clear_all()
    
    def close_context_menu(self):
        """安全地关闭右键菜单"""
        if hasattr(self, 'context_menu'):
            try:
                if self.root.winfo_exists():
                    print("正在关闭右键菜单...")
                    self.context_menu.unpost()
                    # 强制处理待处理的事件
                    self.root.update_idletasks()
                    print("右键菜单已关闭")
            except Exception as e:
                print(f"关闭菜单时发生错误: {e}")

    def toggle_style(self):
        """切换配色方案"""
        if self.current_theme == "pink":
            self.apply_theme("black")
        elif self.current_theme == "black":
            self.apply_theme("white")
        elif self.current_theme == "white":
            self.apply_theme("cyberpunk")
        else:
            self.apply_theme("pink")
    
    def make_draggable(self):
        """使窗口可拖拽"""
        def start_drag(event):
            self.root.x = event.x
            self.root.y = event.y
        
        def do_drag(event):
            x = self.root.winfo_x() + (event.x - self.root.x)
            y = self.root.winfo_y() + (event.y - self.root.y)
            self.root.geometry(f"+{x}+{y}")
        
        # 绑定拖拽事件到时钟区域（避免与TodoList交互冲突）
        for widget in [self.clock_frame, self.time_label, self.date_label]:
            widget.bind("<Button-1>", start_drag)
            widget.bind("<B1-Motion>", do_drag)
    
    def update_time(self):
        """更新时间显示"""
        now = datetime.now()
        
        # 格式化时间显示
        time_str = now.strftime("%H:%M:%S")
        self.time_label.config(text=time_str)
        
        # 格式化日期显示
        weekdays = ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
        weekday = weekdays[now.weekday()]
        date_str = now.strftime(f"%Y-%m-%d {weekday}")
        self.date_label.config(text=date_str)
        
        # 每1000毫秒（1秒）更新一次
        try:
            if self.root.winfo_exists():
                self.root.after(1000, self.update_time)
        except:
            pass
    
    def quit_app(self):
        """退出应用"""
        self.root.quit()
        self.root.destroy()
        sys.exit()
    
    def show_statistics(self):
        """生成并显示统计报告"""
        try:
            stats_file = self.generate_statistics_html()
            # 在默认浏览器中打开统计页面
            webbrowser.open(f'file:///{os.path.abspath(stats_file)}')
            print(f"统计报告已生成: {stats_file}")
        except Exception as e:
            print(f"生成统计报告失败: {e}")
    
    def calculate_statistics(self):
        """计算任务统计数据"""
        today = datetime.now().strftime('%Y-%m-%d')
        
        # 当日完成的任务
        today_completed = [t for t in self.todos if t['completed'] and 
                          t.get('completed_at', '').startswith(today)]
        
        # 当日创建的任务
        today_created = [t for t in self.todos if 
                        t.get('created_at', '').startswith(today)]
        
        stats = {
            'total_tasks': len(self.todos),
            'completed_tasks': len([t for t in self.todos if t['completed']]),
            'pending_tasks': len([t for t in self.todos if not t['completed']]),
            'today_completed': len(today_completed),
            'today_created': len(today_created),
            'completion_rate': 0,
            'today_completion_rate': 0,
            'task_lengths': defaultdict(int),
            'recent_tasks': [],
            'today_completed_tasks': today_completed,
            'longest_task': '',
            'shortest_task': ''
        }
        
        if stats['total_tasks'] > 0:
            stats['completion_rate'] = round((stats['completed_tasks'] / stats['total_tasks']) * 100, 1)
        
        if stats['today_created'] > 0:
            stats['today_completion_rate'] = round((stats['today_completed'] / stats['today_created']) * 100, 1)
        
        if len(self.todos) > 0:
            # 任务完成时间分布
            for todo in self.todos:
                if todo.get('completed', False) and todo.get('created_at') and todo.get('completed_at'):
                    try:
                        created = datetime.fromisoformat(todo['created_at'])
                        completed = datetime.fromisoformat(todo['completed_at'])
                        duration = completed - created
                        hours = duration.total_seconds() / 3600
                        
                        if hours <= 1:
                            stats['task_lengths']['1小时内'] += 1
                        elif hours <= 24:
                            stats['task_lengths']['1天内'] += 1
                        elif hours <= 168:  # 7 days
                            stats['task_lengths']['1周内'] += 1
                        else:
                            stats['task_lengths']['超过1周'] += 1
                    except:
                        pass  # 忽略时间格式错误
            
            # 最快和最慢完成的任务
            completed_tasks = [t for t in self.todos if t.get('completed', False) and t.get('created_at') and t.get('completed_at')]
            if completed_tasks:
                try:
                    tasks_with_duration = []
                    for task in completed_tasks:
                        created = datetime.fromisoformat(task['created_at'])
                        completed = datetime.fromisoformat(task['completed_at'])
                        duration = completed - created
                        tasks_with_duration.append((task, duration.total_seconds()))
                    
                    if tasks_with_duration:
                        tasks_with_duration.sort(key=lambda x: x[1])  # 按完成时间排序
                        stats['shortest_task'] = f"{tasks_with_duration[0][0]['text']} ({tasks_with_duration[0][1]/3600:.1f}小时)"
                        stats['longest_task'] = f"{tasks_with_duration[-1][0]['text']} ({tasks_with_duration[-1][1]/3600:.1f}小时)"
                except:
                    pass
            
            # 最近的任务（按更新时间排序）
            recent_sorted = sorted(self.todos, 
                                 key=lambda x: x.get('updated_at', x.get('created_at', '')), 
                                 reverse=True)
            stats['recent_tasks'] = recent_sorted[:5]
        
        return stats
    
    def generate_statistics_html(self):
        """生成HTML统计报告"""
        stats = self.calculate_statistics()
        
        html_content = f"""<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>📊 桌面时钟 - 任务统计报告</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <style>
        @import url('https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap');
        body {{ font-family: 'Inter', sans-serif; }}
        .gradient-bg {{ background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); }}
        .card-shadow {{ box-shadow: 0 10px 25px rgba(0,0,0,0.1); }}
        .stat-card {{ transition: transform 0.3s ease, box-shadow 0.3s ease; }}
        .stat-card:hover {{ transform: translateY(-5px); box-shadow: 0 15px 35px rgba(0,0,0,0.15); }}
    </style>
</head>
<body class="bg-gray-50 min-h-screen">
    <!-- Header -->
    <div class="gradient-bg text-white py-12">
        <div class="container mx-auto px-6">
            <div class="text-center">
                <h1 class="text-4xl font-bold mb-4">📊 任务统计报告</h1>
                <p class="text-xl opacity-90">桌面时钟 TodoList 数据分析</p>
                <p class="text-sm opacity-75 mt-2">生成时间: {datetime.now().strftime('%Y-%m-%d %H:%M:%S')}</p>
            </div>
        </div>
    </div>
    
    <!-- Stats Overview -->
    <div class="container mx-auto px-6 -mt-8">
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-12">
            <!-- Total Tasks -->
            <div class="stat-card bg-white rounded-xl p-6 card-shadow">
                <div class="flex items-center">
                    <div class="p-3 rounded-full bg-blue-100 text-blue-600">
                        <svg class="w-8 h-8" fill="currentColor" viewBox="0 0 20 20">
                            <path d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"></path>
                        </svg>
                    </div>
                    <div class="ml-4">
                        <p class="text-sm font-medium text-gray-600">总任务数</p>
                        <p class="text-2xl font-bold text-gray-900">{stats['total_tasks']}</p>
                    </div>
                </div>
            </div>
            
            <!-- Completed Tasks -->
            <div class="stat-card bg-white rounded-xl p-6 card-shadow">
                <div class="flex items-center">
                    <div class="p-3 rounded-full bg-green-100 text-green-600">
                        <svg class="w-8 h-8" fill="currentColor" viewBox="0 0 20 20">
                            <path fill-rule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clip-rule="evenodd"></path>
                        </svg>
                    </div>
                    <div class="ml-4">
                        <p class="text-sm font-medium text-gray-600">已完成</p>
                        <p class="text-2xl font-bold text-green-600">{stats['completed_tasks']}</p>
                    </div>
                </div>
            </div>
            
            <!-- Pending Tasks -->
            <div class="stat-card bg-white rounded-xl p-6 card-shadow">
                <div class="flex items-center">
                    <div class="p-3 rounded-full bg-yellow-100 text-yellow-600">
                        <svg class="w-8 h-8" fill="currentColor" viewBox="0 0 20 20">
                            <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm1-12a1 1 0 10-2 0v4a1 1 0 00.293.707l2.828 2.829a1 1 0 101.415-1.415L11 9.586V6z" clip-rule="evenodd"></path>
                        </svg>
                    </div>
                    <div class="ml-4">
                        <p class="text-sm font-medium text-gray-600">待完成</p>
                        <p class="text-2xl font-bold text-yellow-600">{stats['pending_tasks']}</p>
                    </div>
                </div>
            </div>
            
            <!-- Completion Rate -->
            <div class="stat-card bg-white rounded-xl p-6 card-shadow">
                <div class="flex items-center">
                    <div class="p-3 rounded-full bg-purple-100 text-purple-600">
                        <svg class="w-8 h-8" fill="currentColor" viewBox="0 0 20 20">
                            <path fill-rule="evenodd" d="M3 4a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1zm0 4a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1zm0 4a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1zm0 4a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1z" clip-rule="evenodd"></path>
                        </svg>
                    </div>
                    <div class="ml-4">
                        <p class="text-sm font-medium text-gray-600">总完成率</p>
                        <p class="text-2xl font-bold text-purple-600">{stats['completion_rate']}%</p>
                    </div>
                </div>
            </div>
        </div>
        
        <!-- Today's Stats -->
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 mb-12">
            <!-- Today Completed -->
            <div class="stat-card bg-white rounded-xl p-6 card-shadow">
                <div class="flex items-center">
                    <div class="p-3 rounded-full bg-emerald-100 text-emerald-600">
                        <svg class="w-8 h-8" fill="currentColor" viewBox="0 0 20 20">
                            <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clip-rule="evenodd"></path>
                        </svg>
                    </div>
                    <div class="ml-4">
                        <p class="text-sm font-medium text-gray-600">今日完成</p>
                        <p class="text-2xl font-bold text-emerald-600">{stats['today_completed']}</p>
                    </div>
                </div>
            </div>
            
            <!-- Today Created -->
            <div class="stat-card bg-white rounded-xl p-6 card-shadow">
                <div class="flex items-center">
                    <div class="p-3 rounded-full bg-indigo-100 text-indigo-600">
                        <svg class="w-8 h-8" fill="currentColor" viewBox="0 0 20 20">
                            <path fill-rule="evenodd" d="M10 3a1 1 0 011 1v5h5a1 1 0 110 2h-5v5a1 1 0 11-2 0v-5H4a1 1 0 110-2h5V4a1 1 0 011-1z" clip-rule="evenodd"></path>
                        </svg>
                    </div>
                    <div class="ml-4">
                        <p class="text-sm font-medium text-gray-600">今日创建</p>
                        <p class="text-2xl font-bold text-indigo-600">{stats['today_created']}</p>
                    </div>
                </div>
            </div>
            
            <!-- Today Completion Rate -->
            <div class="stat-card bg-white rounded-xl p-6 card-shadow">
                <div class="flex items-center">
                    <div class="p-3 rounded-full bg-rose-100 text-rose-600">
                        <svg class="w-8 h-8" fill="currentColor" viewBox="0 0 20 20">
                            <path fill-rule="evenodd" d="M12 7a1 1 0 110-2h5a1 1 0 011 1v5a1 1 0 11-2 0V8.414l-4.293 4.293a1 1 0 01-1.414 0L8 10.414l-4.293 4.293a1 1 0 01-1.414-1.414l5-5a1 1 0 011.414 0L11 10.586 14.586 7H12z" clip-rule="evenodd"></path>
                        </svg>
                    </div>
                    <div class="ml-4">
                        <p class="text-sm font-medium text-gray-600">今日完成率</p>
                        <p class="text-2xl font-bold text-rose-600">{stats['today_completion_rate']}%</p>
                    </div>
                </div>
            </div>
        </div>
        
        <!-- Charts Section -->
        <div class="grid grid-cols-1 lg:grid-cols-2 gap-8 mb-12">
            <!-- Completion Chart -->
            <div class="bg-white rounded-xl p-6 card-shadow">
                <h3 class="text-xl font-bold text-gray-900 mb-4">📈 任务完成分布</h3>
                <canvas id="completionChart" width="400" height="300"></canvas>
            </div>
            
            <!-- Task Completion Time Distribution -->
            <div class="bg-white rounded-xl p-6 card-shadow">
                <h3 class="text-xl font-bold text-gray-900 mb-4">⏱️ 任务完成时间分布</h3>
                <canvas id="lengthChart" width="400" height="300"></canvas>
            </div>
        </div>
        
        <!-- Task Details -->
        <div class="grid grid-cols-1 lg:grid-cols-2 gap-8 mb-12">
            <!-- Recent Tasks -->
            <div class="bg-white rounded-xl p-6 card-shadow">
                <h3 class="text-xl font-bold text-gray-900 mb-4">📋 本周已完成任务</h3>
                <div class="space-y-3">"""
        
        # 添加本周已完成任务列表
        if stats['recent_tasks']:
            # 按日期分组
            from collections import defaultdict
            tasks_by_date = defaultdict(list)
            
            for task in stats['recent_tasks']:
                if task.get('completed_at'):
                    try:
                        completed_time = datetime.fromisoformat(task['completed_at'])
                        date_key = completed_time.strftime('%m-%d')
                        tasks_by_date[date_key].append(task['text'])
                    except:
                        pass
            
            # 按日期排序显示
            html_content += """<div class="space-y-3">"""
            for date_key in sorted(tasks_by_date.keys(), reverse=True):
                task_list = tasks_by_date[date_key]
                html_content += f"""
                    <div class="flex flex-wrap items-center gap-2">
                        <span class="font-semibold text-sm" style="color: #ff69b4;">{date_key}：</span>"""
                
                for task_text in task_list:
                    html_content += f"""
                        <div class="inline-flex items-center px-2 py-1 bg-green-50 text-green-800 rounded-full border border-green-200" style="font-size: 14px;">
                            <span class="font-medium">{task_text}</span>
                        </div>"""
                
                html_content += """
                    </div>"""
            html_content += """</div>"""
        else:
            html_content += """
                    <div class="text-center py-8 text-gray-500">
                        <p>本周暂无已完成任务</p>
                    </div>"""
        
        # 继续HTML内容
        efficiency_message = '🌟 效率很高！' if stats['completion_rate'] >= 80 else '💪 继续努力！' if stats['completion_rate'] >= 50 else '🎯 加油提升！'
        
        html_content += f"""
                </div>
            </div>
            
            <!-- Task Insights -->
            <div class="bg-white rounded-xl p-6 card-shadow">
                <h3 class="text-xl font-bold text-gray-900 mb-4">💡 任务洞察</h3>
                <div class="space-y-4">
                    <div class="p-4 bg-blue-50 rounded-lg">
                        <h4 class="font-semibold text-blue-900 mb-2">🐌 最慢完成任务</h4>
                        <p class="text-blue-800 text-sm">{stats['longest_task'] if stats['longest_task'] else '暂无已完成任务'}</p>
                    </div>
                    <div class="p-4 bg-green-50 rounded-lg">
                        <h4 class="font-semibold text-green-900 mb-2">⚡ 最快完成任务</h4>
                        <p class="text-green-800 text-sm">{stats['shortest_task'] if stats['shortest_task'] else '暂无已完成任务'}</p>
                    </div>
                    <div class="p-4 bg-purple-50 rounded-lg">
                        <h4 class="font-semibold text-purple-900 mb-2">🏆 效率评估</h4>
                        <p class="text-purple-800 text-sm">
                            {efficiency_message} 完成率 {stats['completion_rate']}%
                        </p>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Footer -->
    <footer class="bg-gray-800 text-white py-8">
        <div class="container mx-auto px-6 text-center">
            <p class="text-gray-400">Generated by Desktop Clock TodoList • {datetime.now().year}</p>
        </div>
    </footer>
    
    <script>
        // Completion Chart
        const completionCtx = document.getElementById('completionChart').getContext('2d');
        new Chart(completionCtx, {{
            type: 'doughnut',
            data: {{
                labels: ['已完成', '待完成'],
                datasets: [{{
                    data: [{stats['completed_tasks']}, {stats['pending_tasks']}],
                    backgroundColor: ['#10B981', '#F59E0B'],
                    borderWidth: 0
                }}]
            }},
            options: {{
                responsive: true,
                plugins: {{
                    legend: {{
                        position: 'bottom',
                        labels: {{
                            padding: 20,
                            font: {{
                                size: 14
                            }}
                        }}
                    }}
                }}
            }}
        }});
        
        // Task Completion Time Distribution Chart
        const lengthCtx = document.getElementById('lengthChart').getContext('2d');
        const lengthData = {dict(stats['task_lengths'])};
        new Chart(lengthCtx, {{
            type: 'bar',
            data: {{
                labels: Object.keys(lengthData),
                datasets: [{{
                    label: '完成任务数量',
                    data: Object.values(lengthData),
                    backgroundColor: ['#10B981', '#3B82F6', '#F59E0B', '#EF4444'],
                    borderRadius: 8,
                    borderSkipped: false,
                }}]
            }},
            options: {{
                responsive: true,
                plugins: {{
                    legend: {{
                        display: false
                    }}
                }},
                scales: {{
                    y: {{
                        beginAtZero: true,
                        grid: {{
                            color: '#F3F4F6'
                        }}
                    }},
                    x: {{
                        grid: {{
                            display: false
                        }}
                    }}
                }}
            }}
        }});
    </script>
</body>
</html>"""
        
        # 保存HTML文件
        stats_file = 'todo_statistics.html'
        with open(stats_file, 'w', encoding='utf-8') as f:
            f.write(html_content)
        
        return stats_file
    
    def run(self):
        """运行时钟应用"""
        try:
            # 让窗口获得焦点后立即失去焦点，避免影响其他应用
            self.root.after(100, lambda: self.root.attributes('-topmost', True))
            self.root.mainloop()
        except KeyboardInterrupt:
            self.quit_app()

def main():
    """主函数"""
    print("启动桌面时钟 + TodoList...")
    print("功能说明：")
    print("🕐 时钟功能：")
    print("  - 拖拽时钟区域可移动位置")
    print("  - 右键点击可切换样式")
    print("📋 TodoList功能：")
    print("  - 输入任务后按Enter或点击➕添加")
    print("  - 点击⚪/✅切换完成状态")
    print("  - 点击🗑️删除任务")
    print("  - 任务自动保存到desktop_todos.json")
    print("🖱️ 右键菜单：")
    print("  - 查看统计、隐藏/显示任务、清除任务、退出程序")
    print("⌨️ Ctrl+C 也可退出程序")
    
    try:
        clock = DesktopClock()
        clock.run()
    except Exception as e:
        print(f"程序运行出错: {e}")
        input("按Enter键退出...")

if __name__ == "__main__":
    main()
