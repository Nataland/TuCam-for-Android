package com.android.example.nataland.preview

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.PointF
import androidx.annotation.DrawableRes
import com.android.example.nataland.R
import jp.co.cyberagent.android.gpuimage.filter.*

object GPUImageFilterUtils {

    data class FilterInfo(
        val name: String,
        val filterType: FilterType,
        @DrawableRes val drawableRes: Int
    )

    val defaultFilters: List<FilterInfo> = listOf(
        FilterInfo("Original", FilterType.NO_FILTER, R.drawable.original_demo),
        FilterInfo("Sepia", FilterType.SEPIA, R.drawable.sepia_demo),
        FilterInfo("Grayscale", FilterType.GRAYSCALE, R.drawable.grayscale_demo),
        FilterInfo("Monochrome", FilterType.MONOCHROME, R.drawable.monochrome_demo),
        FilterInfo("Vignette", FilterType.VIGNETTE, R.drawable.vignette_demo),
        FilterInfo("Lookup (Amatorka)", FilterType.LOOKUP_AMATORKA, R.drawable.amatorka_demo),
        FilterInfo("Smooth Toon", FilterType.SMOOTH_TOON, R.drawable.smooth_toon_demo),
        FilterInfo("False Color", FilterType.FALSE_COLOR, R.drawable.false_color_demo),
        FilterInfo("Zoom Blur", FilterType.ZOOM_BLUR, R.drawable.zoom_blur_demo),
        FilterInfo("Vibrance", FilterType.VIBRANCE, R.drawable.vibrance_demo)
    )

    fun createFilterForType(context: Context, type: FilterType): GPUImageFilter {
        return when (type) {
            FilterType.CONTRAST -> GPUImageContrastFilter(2.0f)
            FilterType.GAMMA -> GPUImageGammaFilter(2.0f)
            FilterType.INVERT -> GPUImageColorInvertFilter()
            FilterType.PIXELATION -> GPUImagePixelationFilter()
            FilterType.HUE -> GPUImageHueFilter(90.0f)
            FilterType.BRIGHTNESS -> GPUImageBrightnessFilter(1.5f)
            FilterType.GRAYSCALE -> GPUImageGrayscaleFilter()
            FilterType.SEPIA -> GPUImageSepiaToneFilter()
            FilterType.SHARPEN -> GPUImageSharpenFilter()
            FilterType.SOBEL_EDGE_DETECTION -> GPUImageSobelEdgeDetectionFilter()
            FilterType.THRESHOLD_EDGE_DETECTION -> GPUImageThresholdEdgeDetectionFilter()
            FilterType.THREE_X_THREE_CONVOLUTION -> GPUImage3x3ConvolutionFilter()
            FilterType.EMBOSS -> GPUImageEmbossFilter()
            FilterType.POSTERIZE -> GPUImagePosterizeFilter()
            FilterType.FILTER_GROUP -> GPUImageFilterGroup(
                listOf(
                    GPUImageContrastFilter(),
                    GPUImageDirectionalSobelEdgeDetectionFilter(),
                    GPUImageGrayscaleFilter()
                )
            )
            FilterType.SATURATION -> GPUImageSaturationFilter(1.0f)
            FilterType.EXPOSURE -> GPUImageExposureFilter(0.0f)
            FilterType.HIGHLIGHT_SHADOW -> GPUImageHighlightShadowFilter(
                0.0f,
                1.0f
            )
            FilterType.MONOCHROME -> GPUImageMonochromeFilter(
                1.0f, floatArrayOf(0.6f, 0.45f, 0.3f, 1.0f)
            )
            FilterType.OPACITY -> GPUImageOpacityFilter(1.0f)
            FilterType.RGB -> GPUImageRGBFilter(1.0f, 1.0f, 1.0f)
            FilterType.WHITE_BALANCE -> GPUImageWhiteBalanceFilter(
                5000.0f,
                0.0f
            )
            FilterType.VIGNETTE -> GPUImageVignetteFilter(
                PointF(0.5f, 0.5f),
                floatArrayOf(0.0f, 0.0f, 0.0f),
                0.3f,
                0.75f
            )
            FilterType.TONE_CURVE -> GPUImageToneCurveFilter().apply {
                setFromCurveFileInputStream(context.resources.openRawResource(R.raw.tone_cuver_sample))
            }
            FilterType.LUMINANCE -> GPUImageLuminanceFilter()
            FilterType.LUMINANCE_THRESHSOLD -> GPUImageLuminanceThresholdFilter(0.5f)
            FilterType.BLEND_DIFFERENCE -> createBlendFilter(
                context,
                GPUImageDifferenceBlendFilter::class.java
            )
            FilterType.BLEND_SOURCE_OVER -> createBlendFilter(
                context,
                GPUImageSourceOverBlendFilter::class.java
            )
            FilterType.BLEND_COLOR_BURN -> createBlendFilter(
                context,
                GPUImageColorBurnBlendFilter::class.java
            )
            FilterType.BLEND_COLOR_DODGE -> createBlendFilter(
                context,
                GPUImageColorDodgeBlendFilter::class.java
            )
            FilterType.BLEND_DARKEN -> createBlendFilter(
                context,
                GPUImageDarkenBlendFilter::class.java
            )
            FilterType.BLEND_DISSOLVE -> createBlendFilter(
                context,
                GPUImageDissolveBlendFilter::class.java
            )
            FilterType.BLEND_EXCLUSION -> createBlendFilter(
                context,
                GPUImageExclusionBlendFilter::class.java
            )

            FilterType.BLEND_HARD_LIGHT -> createBlendFilter(
                context,
                GPUImageHardLightBlendFilter::class.java
            )
            FilterType.BLEND_LIGHTEN -> createBlendFilter(
                context,
                GPUImageLightenBlendFilter::class.java
            )
            FilterType.BLEND_ADD -> createBlendFilter(
                context,
                GPUImageAddBlendFilter::class.java
            )
            FilterType.BLEND_DIVIDE -> createBlendFilter(
                context,
                GPUImageDivideBlendFilter::class.java
            )
            FilterType.BLEND_MULTIPLY -> createBlendFilter(
                context,
                GPUImageMultiplyBlendFilter::class.java
            )
            FilterType.BLEND_OVERLAY -> createBlendFilter(
                context,
                GPUImageOverlayBlendFilter::class.java
            )
            FilterType.BLEND_SCREEN -> createBlendFilter(
                context,
                GPUImageScreenBlendFilter::class.java
            )
            FilterType.BLEND_ALPHA -> createBlendFilter(
                context,
                GPUImageAlphaBlendFilter::class.java
            )
            FilterType.BLEND_COLOR -> createBlendFilter(
                context,
                GPUImageColorBlendFilter::class.java
            )
            FilterType.BLEND_HUE -> createBlendFilter(
                context,
                GPUImageHueBlendFilter::class.java
            )
            FilterType.BLEND_SATURATION -> createBlendFilter(
                context,
                GPUImageSaturationBlendFilter::class.java
            )
            FilterType.BLEND_LUMINOSITY -> createBlendFilter(
                context,
                GPUImageLuminosityBlendFilter::class.java
            )
            FilterType.BLEND_LINEAR_BURN -> createBlendFilter(
                context,
                GPUImageLinearBurnBlendFilter::class.java
            )
            FilterType.BLEND_SOFT_LIGHT -> createBlendFilter(
                context,
                GPUImageSoftLightBlendFilter::class.java
            )
            FilterType.BLEND_SUBTRACT -> createBlendFilter(
                context,
                GPUImageSubtractBlendFilter::class.java
            )
            FilterType.BLEND_CHROMA_KEY -> createBlendFilter(
                context,
                GPUImageChromaKeyBlendFilter::class.java
            )
            FilterType.BLEND_NORMAL -> createBlendFilter(
                context,
                GPUImageNormalBlendFilter::class.java
            )

            FilterType.LOOKUP_AMATORKA -> GPUImageLookupFilter().apply {
                bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.lookup_amatorka)
            }
            FilterType.GAUSSIAN_BLUR -> GPUImageGaussianBlurFilter()
            FilterType.CROSSHATCH -> GPUImageCrosshatchFilter()
            FilterType.BOX_BLUR -> GPUImageBoxBlurFilter()
            FilterType.CGA_COLORSPACE -> GPUImageCGAColorspaceFilter()
            FilterType.DILATION -> GPUImageDilationFilter()
            FilterType.KUWAHARA -> GPUImageKuwaharaFilter()
            FilterType.RGB_DILATION -> GPUImageRGBDilationFilter()
            FilterType.SKETCH -> GPUImageSketchFilter()
            FilterType.TOON -> GPUImageToonFilter()
            FilterType.SMOOTH_TOON -> GPUImageSmoothToonFilter()
            FilterType.BULGE_DISTORTION -> GPUImageBulgeDistortionFilter()
            FilterType.GLASS_SPHERE -> GPUImageGlassSphereFilter()
            FilterType.HAZE -> GPUImageHazeFilter()
            FilterType.LAPLACIAN -> GPUImageLaplacianFilter()
            FilterType.NON_MAXIMUM_SUPPRESSION -> GPUImageNonMaximumSuppressionFilter()
            FilterType.SPHERE_REFRACTION -> GPUImageSphereRefractionFilter()
            FilterType.SWIRL -> GPUImageSwirlFilter()
            FilterType.WEAK_PIXEL_INCLUSION -> GPUImageWeakPixelInclusionFilter()
            FilterType.FALSE_COLOR -> GPUImageFalseColorFilter()
            FilterType.COLOR_BALANCE -> GPUImageColorBalanceFilter()
            FilterType.LEVELS_FILTER_MIN -> GPUImageLevelsFilter()
            FilterType.HALFTONE -> GPUImageHalftoneFilter()
            FilterType.BILATERAL_BLUR -> GPUImageBilateralBlurFilter()
            FilterType.ZOOM_BLUR -> GPUImageZoomBlurFilter()
            FilterType.TRANSFORM2D -> GPUImageTransformFilter()
            FilterType.SOLARIZE -> GPUImageSolarizeFilter()
            FilterType.VIBRANCE -> GPUImageVibranceFilter(1.2f)
            FilterType.NO_FILTER -> GPUImageFilter()
        }
    }

    private fun createBlendFilter(
        context: Context,
        filterClass: Class<out GPUImageTwoInputFilter>
    ): GPUImageFilter {
        return try {
            filterClass.newInstance().apply {
                bitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            GPUImageFilter()
        }
    }

    enum class FilterType {
        NO_FILTER, CONTRAST, GRAYSCALE, SHARPEN, SEPIA, SOBEL_EDGE_DETECTION, THRESHOLD_EDGE_DETECTION, THREE_X_THREE_CONVOLUTION, FILTER_GROUP, EMBOSS, POSTERIZE, GAMMA, BRIGHTNESS, INVERT, HUE, PIXELATION,
        SATURATION, EXPOSURE, HIGHLIGHT_SHADOW, MONOCHROME, OPACITY, RGB, WHITE_BALANCE, VIGNETTE, TONE_CURVE, LUMINANCE, LUMINANCE_THRESHSOLD, BLEND_COLOR_BURN, BLEND_COLOR_DODGE, BLEND_DARKEN,
        BLEND_DIFFERENCE, BLEND_DISSOLVE, BLEND_EXCLUSION, BLEND_SOURCE_OVER, BLEND_HARD_LIGHT, BLEND_LIGHTEN, BLEND_ADD, BLEND_DIVIDE, BLEND_MULTIPLY, BLEND_OVERLAY, BLEND_SCREEN, BLEND_ALPHA,
        BLEND_COLOR, BLEND_HUE, BLEND_SATURATION, BLEND_LUMINOSITY, BLEND_LINEAR_BURN, BLEND_SOFT_LIGHT, BLEND_SUBTRACT, BLEND_CHROMA_KEY, BLEND_NORMAL, LOOKUP_AMATORKA,
        GAUSSIAN_BLUR, CROSSHATCH, BOX_BLUR, CGA_COLORSPACE, DILATION, KUWAHARA, RGB_DILATION, SKETCH, TOON, SMOOTH_TOON, BULGE_DISTORTION, GLASS_SPHERE, HAZE, LAPLACIAN, NON_MAXIMUM_SUPPRESSION,
        SPHERE_REFRACTION, SWIRL, WEAK_PIXEL_INCLUSION, FALSE_COLOR, COLOR_BALANCE, LEVELS_FILTER_MIN, BILATERAL_BLUR, ZOOM_BLUR, HALFTONE, TRANSFORM2D, SOLARIZE, VIBRANCE
    }
}
