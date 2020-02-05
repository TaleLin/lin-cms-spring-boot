---
name: 提出一个bug
about: 提出bug帮助我们完善项目
---

**描述 bug**

- 你是如何操作的？
- 发生了什么？
- 你觉得应该出现什么？

**你使用哪个版本出现该问题？**

如果使用`master`，请表明是 master 分支，否则给出具体的版本号

**如何再现**

If your bug is deterministic, can you give a minimal reproducing code?
Some bugs are not deterministic. Can you describe with precision in which context it happened?
If this is possible, can you share your code?

如果你确定存在这个 bug，你能提供我们一个最小的实现代码吗？
一些 bug 是不确定，只会在某些条件下触发，你能详细描述一下具体的情况和提供复现的步骤吗？
当然如果你提供在线的 repo，那就再好不过了。

如果你发现了 bug，并修复了它，请用`git rebase`合并成一条标准的`fix: description`提交，然后向我们的
项目提 PR，我们会在第一时间审核，并感谢您的参与。
