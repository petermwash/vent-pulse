"use client";

import { motion } from "framer-motion";
import { useEffect, useRef } from "react";

import type { BubbleDatum } from "@/components/dashboard/dashboard-data";

type AtmosphereCanvasProps = {
  bubbles: BubbleDatum[];
};

type BubbleParticle = BubbleDatum & {
  offsetX: number;
  offsetY: number;
};

const clamp = (value: number, min: number, max: number) =>
  Math.min(max, Math.max(min, value));

export function AtmosphereCanvas({ bubbles }: AtmosphereCanvasProps) {
  const hostRef = useRef<HTMLDivElement | null>(null);
  const canvasRef = useRef<HTMLCanvasElement | null>(null);

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
      fieldGradient.addColorStop(0, "rgba(184, 168, 255, 0.18)");
      fieldGradient.addColorStop(0.4, "rgba(143, 214, 255, 0.08)");
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
        const radius = Math.max(70, Math.min(width, height) * particle.size);
        const glowRadius = radius * 1.9;
        const color = getComputedStyle(document.documentElement)
          .getPropertyValue(particle.colorVar)
          .trim();

        drawGlow(
          x,
          y,
          glowRadius,
          particle.glow,
          `${color}55`,
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
      className="relative min-h-[28rem] overflow-hidden rounded-[2rem] md:min-h-[36rem] xl:min-h-[44rem]"
    >
      <canvas ref={canvasRef} className="absolute inset-0 h-full w-full" />
      <div className="atmosphere-sheen absolute inset-0" />
      {bubbles.map((bubble, index) => (
        <motion.article
          key={bubble.id}
          className="glass-panel absolute flex aspect-square items-center justify-center rounded-full border border-white/40 text-center"
          style={{
            left: `${bubble.x * 100}%`,
            top: `${bubble.y * 100}%`,
            width: `${bubble.size * 100}%`,
            minWidth: "6rem",
            transform: "translate(-50%, -50%)",
            background: `color-mix(in srgb, var(${bubble.colorVar}) 46%, white)`,
            boxShadow: `0 0 0 1px rgba(255,255,255,0.3), 0 0 46px ${bubble.glow}`,
            opacity: bubble.opacity ?? 1,
          }}
          initial={{ opacity: 0, scale: 0.92 }}
          animate={{
            opacity: bubble.opacity ?? 1,
            scale: 1,
            x: [0, bubble.velocityX * 26, 0],
            y: [0, bubble.velocityY * 34, 0],
          }}
          transition={{
            opacity: { duration: 0.5, delay: index * 0.05 },
            scale: { duration: 0.5, delay: index * 0.05 },
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
          }}
        >
          <div className="flex flex-col items-center gap-1 px-4 text-balance">
            <span className="text-lg font-semibold leading-none text-[var(--foreground)] sm:text-[1.45rem]">
              {bubble.label}
            </span>
            <span className="text-base text-[var(--foreground-soft)] sm:text-[1.1rem]">
              {bubble.value}
            </span>
          </div>
        </motion.article>
      ))}
    </div>
  );
}
