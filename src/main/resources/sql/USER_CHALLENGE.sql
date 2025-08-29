INSERT INTO user_challenge (user_id, challenge_id, repository_url, start_tier, end_tier, total_solved_problem, today_solved_problem, total_penalty, today_solved, is_end) VALUES
-- 하은 (user_id = 1)
    (1, 1, 'https://github.com/jhe93/algorithm-study-1', 'SILVER_III', 'SILVER_II', 25, 0, 9000, false, true),
    (1, 2, 'https://github.com/jhe93/baekjoon-master-1', 'SILVER_II', 'GOLD_V', 40, 0, 10000, false, true),
    (1, 3, 'https://github.com/jhe93/daily-coding-1', 'GOLD_V', 'GOLD_V', 15, 1, 2000, true, false),
    (1, 4, 'https://github.com/jhe93/problem-solving-1', 'GOLD_V', 'GOLD_V', 20, 2, 0, true, false),
    (1, 5, 'https://github.com/jhe93/algorithm-journey-1', 'GOLD_V', 'GOLD_V', 18, 1, 6000, false, false),
    (1, 6, 'https://github.com/jhe93/coding-workout-1', 'GOLD_V', 'GOLD_V', 12, 1, 1500, true, false),
    (1, 7, 'https://github.com/jhe93/coding-test-prep-1', 'GOLD_V', 'GOLD_V', 8, 2, 16000, false, false),
    (1, 8, 'https://github.com/jhe93/algorithm-practice-1', 'GOLD_V', 'GOLD_V', 10, 1, 3500, false, false),

-- 민지 (user_id = 2)
    (2, 1, 'https://github.com/cmj010202/algorithm-study-2', 'SILVER_IV', 'SILVER_I', 30, 0, 6000, false, true),
    (2, 2, 'https://github.com/cmj010202/baekjoon-master-2', 'SILVER_I', 'GOLD_IV', 45, 0, 5000, false, true),
    (2, 3, 'https://github.com/cmj010202/daily-coding-2', 'GOLD_IV', 'GOLD_IV', 12, 1, 2000, true, false),
    (2, 4, 'https://github.com/cmj010202/problem-solving-2', 'GOLD_IV', 'GOLD_IV', 16, 2, 0, true, false),
    (2, 5, 'https://github.com/cmj010202/algorithm-journey-2', 'GOLD_IV', 'GOLD_IV', 21, 2, 6000, false, false),
    (2, 6, 'https://github.com/cmj010202/coding-workout-2', 'GOLD_IV', 'GOLD_IV', 14, 1, 1500, true, false),
    (2, 7, 'https://github.com/cmj010202/coding-test-prep-2', 'GOLD_IV', 'GOLD_IV', 6, 1, 24000, false, false),
    (2, 8, 'https://github.com/cmj010202/algorithm-practice-2', 'GOLD_IV', 'GOLD_IV', 11, 2, 3500, true, false);
