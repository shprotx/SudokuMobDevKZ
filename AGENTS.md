# SudokuMobDevKZ agent instructions

`CLAUDE.md` is the canonical project configuration for every coding agent in this repository. Read it completely before planning, searching, editing, reviewing, testing, or operating execution workflows.

Claude's Android/KMP project memory is shared with Codex. At the start of a task, read `/Users/artur/.claude/projects/-Users-artur-StudioProjects-SudokuMobDevKZ/memory/MEMORY.md` as the memory index. For work involving the separate iOS port, also read `/Users/artur/.claude/projects/-Users-artur-StudioProjects-SudokuMobDevKZ-iOS/memory/MEMORY.md`. Before acting in an area covered by an entry, read the linked memory file completely. Explicit user instructions and current repository, store, backend, or GitHub state take precedence over memory; verify time-sensitive status.

Use the installed `ast-index` CLI as the primary structural search tool. Prefer `ast-index explore`, `search`, `symbol`, `class`, `usages`, `refs`, `callers`, `hierarchy`, `outline`, `file`, or `map`; use `rg` for plain text, regexes, comments, string literals, configuration, unsupported languages, or when indexed search returns nothing. Check freshness with `ast-index stats` and update stale indexes with `ast-index update`.

Respect the project-specific execution-loop, issue discussion, branching, staging, and visual verification rules from memory. Do not commit, push, merge, release, deploy, create or modify GitHub issues, or start an execution loop unless the user request or project instructions authorize it. Preserve unrelated working-tree changes.
