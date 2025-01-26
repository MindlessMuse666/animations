package com.example.animation

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

class MainActivity : AppCompatActivity() {
    // Переменные для анимации лапок
    private lateinit var paws_animation_button: Button
    private lateinit var paw1: ImageView
    private lateinit var paw2: ImageView
    private lateinit var paw3: ImageView
    private lateinit var paw4: ImageView


    // Переменные для анимации цветочков
    private lateinit var flowers_animation_button: Button
    private lateinit var flower1: ImageView
    private lateinit var flower2: ImageView
    private lateinit var flower3: ImageView
    private lateinit var flower4: ImageView

    private var flowerAnimatorSet: AnimatorSet? = null
    private var isAnimating = false


    // Переменные для анимации машинки
    private lateinit var car_animation_button: Button
    private lateinit var car: ImageView
    private lateinit var car_layout: ConstraintLayout

    private var car_animation_set: AnimatorSet? = null
    private var is_car_animating: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Блок анимации лапок
        paws_animation_button = findViewById(R.id.pawButton)

        paw1 = findViewById(R.id.paw1)
        paw2 = findViewById(R.id.paw2)
        paw3 = findViewById(R.id.paw3)
        paw4 = findViewById(R.id.paw4)

        paws_animation_button.setOnClickListener {
            startPawsAnimation()
        }


        // Блок анимации цветочков
        flower1 = findViewById(R.id.flower1)
        flower2 = findViewById(R.id.flower2)
        flower3 = findViewById(R.id.flower3)
        flower4 = findViewById(R.id.flower4)

        flowers_animation_button = findViewById(R.id.flowerButton)

        flowers_animation_button.setOnClickListener {
            if (isAnimating) {
                stopFlowerAnimation()
            } else {
                startFlowerAnimation()
            }
            isAnimating = !isAnimating
        }


        // Блок анимации машинки
        car_animation_button = findViewById(R.id.carButton)
        car = findViewById(R.id.car)
        car_layout = findViewById(R.id.carLayout)


        car_animation_button.setOnClickListener {
            if (is_car_animating) {
                stopCarAnimation()
            } else {
                startCarAnimation()
            }

            is_car_animating = !is_car_animating
        }
    }


    // Функции анимации лапок
    private fun startPawsAnimation() {
        val animatorSet = AnimatorSet()
        val fadeInDuration = 500L
        val fadeOutDuration = 500L
        val delayBetweenPaws = 200L
        val visibleDelay = 1000L // Задержка, когда лапки видимы

        val paw1FadeIn = createAlphaAnimator(paw1, fadeInDuration, 0f, 1f)
        val paw2FadeIn = createAlphaAnimator(paw2, fadeInDuration, 0f, 1f).apply { startDelay = delayBetweenPaws }
        val paw3FadeIn = createAlphaAnimator(paw3, fadeInDuration, 0f, 1f).apply { startDelay = delayBetweenPaws * 2 }
        val paw4FadeIn = createAlphaAnimator(paw4, fadeInDuration, 0f, 1f).apply { startDelay = delayBetweenPaws * 3 }

        val pawFadeOut = createAlphaAnimator(paw1, fadeOutDuration, 1f, 0f).apply { startDelay = visibleDelay }
        val paw2FadeOut = createAlphaAnimator(paw2, fadeOutDuration, 1f, 0f).apply { startDelay = visibleDelay }
        val paw3FadeOut = createAlphaAnimator(paw3, fadeOutDuration, 1f, 0f).apply { startDelay = visibleDelay }
        val paw4FadeOut = createAlphaAnimator(paw4, fadeOutDuration, 1f, 0f).apply { startDelay = visibleDelay }

        val fadeOutAnimatorSet = AnimatorSet()
        fadeOutAnimatorSet.playTogether(pawFadeOut, paw2FadeOut, paw3FadeOut, paw4FadeOut)


        animatorSet.playSequentially(paw1FadeIn, paw2FadeIn, paw3FadeIn, paw4FadeIn, fadeOutAnimatorSet)
        animatorSet.start()
    }


    private fun createAlphaAnimator(view: ImageView, duration: Long, fromAlpha: Float, toAlpha: Float): ObjectAnimator {
        return ObjectAnimator.ofFloat(view, "alpha", fromAlpha, toAlpha).apply {
            this.duration = duration
            interpolator = AccelerateInterpolator()
        }
    }



    // Функции анимации цветочков
    private fun startFlowerAnimation() {
        val rotateDuration = 3000L
        val delayBetweenFlowers = 500L
        val flowers = listOf(flower1, flower2, flower3, flower4)
        val directions = listOf(1, -1, 1, -1)

        flowerAnimatorSet = AnimatorSet()


        var currentDelay = 0L
        for (i in flowers.indices) {
            val flower = flowers[i]
            val direction = directions[i]

            val animator = createRotationAnimator(flower, rotateDuration, direction)
                .apply { startDelay = currentDelay }

            flowerAnimatorSet?.play(animator)
            currentDelay += delayBetweenFlowers
        }
        flowerAnimatorSet?.start()

    }


    private fun stopFlowerAnimation() {
        flowerAnimatorSet?.pause()
        flowerAnimatorSet?.end()
        flowerAnimatorSet = null
    }


    private fun createRotationAnimator(view: ImageView, duration: Long, direction: Int): ObjectAnimator {
        val rotationStart = view.rotation
        return ObjectAnimator.ofFloat(view, "rotation", rotationStart, rotationStart + (360 * direction)).apply {
            this.duration = duration
            interpolator = LinearInterpolator()
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.RESTART
        }
    }



    // Функции анимации машинки
    private fun startCarAnimation() {
        val fadeInDuration = 250L
        val moveDuration = 2000L
        val fadeOutDuration = 250L

        val startX = car.x
        val startY = car.y
        val endY = -(car_layout.height + car.height).toFloat()
        val endX = car.x

        car_animation_set = AnimatorSet()

        val fadeIn = createAlphaAnimator(car,fadeInDuration, 0f, 1f)
        val move = createTranslateAnimator(car,moveDuration, startX, endX, startY, endY)
        val fadeOut = createAlphaAnimator(car,fadeOutDuration,1f,0f)

        car_animation_set?.playSequentially(fadeIn, move, fadeOut)
        car_animation_set?.start()
    }


    private fun stopCarAnimation() {
        car_animation_set?.cancel()
        car_animation_set = null
        car.alpha = 0f // Возвращаем машинку в начальное состояние (невидимую)

        val newLayoutParams = car_layout.getLayoutParams()
        val layout_height = newLayoutParams.height
        car.y = layout_height.toFloat()
    }


    private fun createTranslateAnimator(view: ImageView, duration: Long, fromX: Float, toX: Float, fromY: Float, toY: Float): ObjectAnimator {
        return ObjectAnimator.ofFloat(view, "y", fromY, toY).apply {
            this.duration = duration
            interpolator = LinearInterpolator()
        }
    }
}