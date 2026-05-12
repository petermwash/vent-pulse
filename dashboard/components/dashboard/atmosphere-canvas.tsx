"use client";

import { motion } from "framer-motion";
import { useEffect, useRef, useState } from "react";

import type { BubbleDatum } from "@/components/dashboard/dashboard-data";

type AtmosphereCanvasProps = {
  bubbles: BubbleDatum[];
  className?: string;
};

type BubbleParticle = BubbleDatum & {
  offsetX: number;
  offsetY: number;
};

const clamp = (value: number, min: number, max: number) =>
  Math.min(max, Math.max(min, value));

export function AtmosphereCanvas({ bubbles, className = "" }: AtmosphereCanvasProps) {
  const hostRef = useRef<HTMLDivElement | null>(null);
  const canvasRef = useRef<HTMLCanvasElement | null>(null);
  const [hoveredBubbleId, setHoveredBubbleId] = useState<string | null>(null);

  useEffect(() => {
    const host = hostRef.current;
    const canvas = canvasRef.current;
    if (!host || !canvas) {
      return;
    }

    const context = canvas.getContext("2d");
    if (!context) {
      return;
    }

    const particles: BubbleParticle[] = bubbles.map((bubble, index) => ({
      ...bubble,
      offsetX: index * 0.8,
      offsetY: index * 1.4,
    }));

    let width = 0;
    let height = 0;
    let animationFrame = 0;
    const pixelRatio = window.devicePixelRatio || 1;

    const resize = () => {
      width = host.clientWidth;
      height = host.clientHeight;
      canvas.width = Math.max(1, Math.floor(width * pixelRatio));
      canvas.height = Math.max(1, Math.floor(height * pixelRatio));
      canvas.style.width = `${width}px`;
      canvas.style.height = `${height}px`;
      context.setTransform(pixelRatio, 0, 0, pixelRatio, 0, 0);
    };

    const drawGlow = (
      x: number,
      y: number,
      radius: number,
      glowColor: string,
      fillColor: string,
      opacity: number,
    ) => {
      const gradient = context.createRadialGradient(x, y, radius * 0.2, x, y, radius);
      gradient.addColorStop(0, fillColor);
      gradient.addColorStop(0.45, glowColor);
      gradient.addColorStop(1, "rgba(255,255,255,0)");

      context.globalAlpha = opacity;
      context.fillStyle = gradient;
      context.beginPath();
      context.arc(x, y, radius, 0, Math.PI * 2);
      context.fill();
    };

    const render = (time: number) => {
      context.clearRect(0, 0, width, height);

      const fieldGradient = context.createLinearGradient(0, 0, width, height);
      fieldGradient.addColorStop(0, "rgba(184, 168, 255, 0.025)");
      fieldGradient.addColorStop(0.44, "rgba(143, 214, 255, 0.018)");
      fieldGradient.addColorStop(1, "rgba(255, 255, 255, 0)");
      context.fillStyle = fieldGradient;
      context.fillRect(0, 0, width, height);

      particles.forEach((particle) => {
        const driftX =
          Math.sin(time * 0.00022 + particle.offsetX) * particle.velocityX * width;
        const driftY =
          Math.cos(time * 0.00018 + particle.offsetY) * particle.velocityY * height;
        const x = clamp(particle.x * width + driftX, width * 0.05, width * 0.95);
        const y = clamp(particle.y * height + driftY, height * 0.05, height * 0.95);
        const radius = Math.max(74, Math.min(width, height) * particle.size * 1.04);
        const glowRadius = radius * 2;
        const color = getComputedStyle(document.documentElement)
          .getPropertyValue(particle.colorVar)
          .trim();

        drawGlow(
          x,
          y,
          glowRadius,
          particle.glow,
          `${color}44`,
          particle.opacity ?? 1,
        );
      });

      animationFrame = window.requestAnimationFrame(render);
    };

    resize();
    const observer = new ResizeObserver(resize);
    observer.observe(host);
    animationFrame = window.requestAnimationFrame(render);

    return () => {
      observer.disconnect();
      window.cancelAnimationFrame(animationFrame);
    };
  }, [bubbles]);

  return (
    <div
      ref={hostRef}
      className={`relative min-h-[28rem] overflow-hidden rounded-[2.4rem] md:min-h-[38rem] xl:min-h-[46rem] ${className}`}
    >
      <div className="absolute inset-0 bg-[linear-gradient(180deg,rgba(255,255,255,0.72),rgba(250,248,255,0.62))]" />
      <canvas ref={canvasRef} className="absolute inset-0 h-full w-full" />
      <div className="atmosphere-sheen absolute inset-0" />
      {bubbles.map((bubble, index) => {
        const isHovered = hoveredBubbleId === bubble.id;

        return (
          <motion.article
            key={bubble.id}
            className="group absolute flex aspect-square items-center justify-center rounded-full border-2 text-center backdrop-blur-sm"
            onMouseEnter={() => setHoveredBubbleId(bubble.id)}
            onMouseLeave={() => setHoveredBubbleId(null)}
            style={{
              left: `${bubble.x * 100}%`,
              top: `${bubble.y * 100}%`,
              width: `${bubble.size * 108}%`,
              minWidth: "6.9rem",
              transform: "translate(-50%, -50%)",
              background: `color-mix(in srgb, var(${bubble.colorVar}) 68%, white 32%)`,
              borderColor: `color-mix(in srgb, var(${bubble.colorVar}) 72%, white 28%)`,
              boxShadow: `0 0 0 1px rgba(255,255,255,0.34), 0 0 32px ${bubble.glow}, 0 0 78px ${bubble.glow}`,
              zIndex: isHovered ? 20 : 1,
            }}
            initial={{ opacity: 0, scale: 0.82 }}
            animate={
              isHovered
                ? { opacity: 1, scale: 1.12, x: 0, y: 0 }
                : {
                    opacity: [
                      0,
                      bubble.opacity ?? 1,
                      bubble.opacity ?? 1,
                      (bubble.opacity ?? 1) * 0.86,
                    ],
                    scale: [0.82, 1, 1.07, 1],
                    x: [0, bubble.velocityX * 26, 0],
                    y: [0, bubble.velocityY * 34, 0],
                  }
            }
            transition={
              isHovered
                ? { duration: 0.25, ease: "easeOut" }
                : {
                    opacity: {
                      duration: 6.5 + index * 0.35,
                      delay: index * 0.18,
                      repeat: Number.POSITIVE_INFINITY,
                      ease: "easeInOut",
                    },
                    scale: {
                      duration: 6.5 + index * 0.35,
                      delay: index * 0.18,
                      repeat: Number.POSITIVE_INFINITY,
                      ease: "easeInOut",
                    },
                    x: {
                      duration: 10 + index,
                      repeat: Number.POSITIVE_INFINITY,
                      repeatType: "mirror",
                      ease: "easeInOut",
                    },
                    y: {
                      duration: 12 + index,
                      repeat: Number.POSITIVE_INFINITY,
                      repeatType: "mirror",
                      ease: "easeInOut",
                    },
                  }
            }
          >
            <div className="pointer-events-none absolute bottom-[calc(100%+0.8rem)] left-1/2 z-30 min-w-32 -translate-x-1/2 rounded-full bg-white/90 px-5 py-3 text-center opacity-0 shadow-[0_18px_44px_rgba(45,42,74,0.16)] ring-1 ring-[rgba(184,168,255,0.32)] backdrop-blur-xl transition duration-200 group-hover:translate-y-[-0.2rem] group-hover:opacity-100">
              <span className="block text-sm font-bold leading-5 text-[var(--foreground)]">
                {bubble.label}
              </span>
              <span className="block text-xs leading-5 text-[var(--foreground-soft)]">
                {bubble.value} People
              </span>
            </div>
            <div className="flex flex-col items-center gap-1 px-4 text-balance">
              <span className="text-xl font-semibold leading-none text-[var(--foreground)] sm:text-[1.65rem]">
                {bubble.label}
              </span>
            </div>
          </motion.article>
        );
      })}
    </div>
  );
}
