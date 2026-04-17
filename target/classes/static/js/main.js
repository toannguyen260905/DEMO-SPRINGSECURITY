/**
 * SocialConnect - Main JavaScript
 * Spring Security Demo App
 */

document.addEventListener('DOMContentLoaded', function () {

    // ── Like Button Animation ──────────────────────────────────────
    document.querySelectorAll('.like-btn').forEach(btn => {
        btn.addEventListener('click', function () {
            const countEl = this.querySelector('span');
            const current = parseInt(countEl.textContent) || 0;

            if (this.classList.contains('liked')) {
                this.classList.remove('liked');
                countEl.textContent = current - 1;
                this.style.color = '';
            } else {
                this.classList.add('liked');
                countEl.textContent = current + 1;
                this.style.color = '#ec4899';

                // Pulse animation
                this.animate([
                    { transform: 'scale(1)' },
                    { transform: 'scale(1.3)' },
                    { transform: 'scale(1)' }
                ], { duration: 300, easing: 'ease-out' });
            }
        });
    });

    // ── Flash message auto-hide ────────────────────────────────────
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(alert => {
        setTimeout(() => {
            alert.style.opacity = '0';
            alert.style.transform = 'translateY(-10px)';
            alert.style.transition = 'all 0.4s ease';
            setTimeout(() => alert.remove(), 400);
        }, 4000);
    });

    // ── Post card hover effect ─────────────────────────────────────
    document.querySelectorAll('.post-card').forEach(card => {
        card.addEventListener('mouseenter', () => {
            card.style.boxShadow = '0 8px 30px rgba(99,102,241,0.1)';
        });
        card.addEventListener('mouseleave', () => {
            card.style.boxShadow = '';
        });
    });

    // ── Stat cards counter animation ───────────────────────────────
    document.querySelectorAll('.stat-number').forEach(el => {
        const target = parseInt(el.textContent);
        if (isNaN(target) || target === 0) return;

        let current = 0;
        const step = Math.ceil(target / 30);
        const timer = setInterval(() => {
            current = Math.min(current + step, target);
            el.textContent = current;
            if (current >= target) clearInterval(timer);
        }, 40);
    });

    // ── Admin table row highlight ──────────────────────────────────
    document.querySelectorAll('.admin-table tbody tr').forEach(row => {
        row.addEventListener('mouseenter', () => {
            row.style.background = 'rgba(99,102,241,0.05)';
        });
        row.addEventListener('mouseleave', () => {
            row.style.background = '';
        });
    });

    // ── Feature cards staggered animation ─────────────────────────
    const featureCards = document.querySelectorAll('.feature-card');
    featureCards.forEach((card, i) => {
        card.style.opacity = '0';
        card.style.transform = 'translateY(30px)';
        setTimeout(() => {
            card.style.transition = 'all 0.5s ease';
            card.style.opacity = '1';
            card.style.transform = 'translateY(0)';
        }, 100 * i);
    });

    // ── Auth page: Fill login form animation ──────────────────────
    const loginForm = document.getElementById('loginForm');
    if (loginForm) {
        loginForm.addEventListener('submit', () => {
            const btn = document.getElementById('loginBtn');
            if (btn) {
                btn.textContent = '🔄 Đang xác thực...';
                btn.disabled = true;
            }
        });
    }

    // ── Demo account click pulse ───────────────────────────────────
    document.querySelectorAll('.demo-account').forEach(el => {
        el.addEventListener('click', function () {
            this.style.background = 'rgba(99,102,241,0.2)';
            setTimeout(() => this.style.background = '', 500);
        });
    });

    // ── Scroll reveal for arch diagram ────────────────────────────
    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.style.opacity = '1';
                entry.target.style.transform = 'translateY(0)';
                observer.unobserve(entry.target);
            }
        });
    }, { threshold: 0.1 });

    document.querySelectorAll('.mvc-box, .arch-layer, .flow-step').forEach(el => {
        el.style.opacity = '0';
        el.style.transform = 'translateY(20px)';
        el.style.transition = 'all 0.5s ease';
        observer.observe(el);
    });

    console.log('%c🛡️ SocialConnect - Spring Security Demo',
        'color: #818cf8; font-size: 16px; font-weight: bold;');
    console.log('%cKiến trúc MVC + Spring Security Filter Chain',
        'color: #94a3b8; font-size: 12px;');
});
