// Minimal theming helper: keeps colors in one place, supports (optional) dark mode.
(() => {
    const palette = {
        primary900: "#000B4F",
        primary700: "#20368F",
        primary300: "#829CD0",
        bg:        "#EBEBEB",
        text:      "#323232",
        muted:     "#6D6D6D"
    };

    function applyTheme(p){
        const r = document.documentElement;
        r.style.setProperty("--c-primary-900", p.primary900);
        r.style.setProperty("--c-primary-700", p.primary700);
        r.style.setProperty("--c-primary-300", p.primary300);
        r.style.setProperty("--c-bg",         p.bg);
        r.style.setProperty("--c-text",       p.text);
        r.style.setProperty("--c-muted",      p.muted);
    }

    // Public API (optional)
    window.PahanaTheme = {
        apply: applyTheme,
        toggleDark(){
            const root = document.documentElement;
            const isDark = root.classList.toggle("theme-dark");
            localStorage.setItem("theme-dark", isDark ? "1" : "0");
        }
    };

    // On load, apply palette + persisted dark state
    applyTheme(palette);
    if (localStorage.getItem("theme-dark") === "1") {
        document.documentElement.classList.add("theme-dark");
    }
})();
