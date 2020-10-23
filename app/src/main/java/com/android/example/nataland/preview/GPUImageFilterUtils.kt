package com.android.example.nataland.preview

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.PointF
import com.android.example.nataland.R
import jp.co.cyberagent.android.gpuimage.filter.*
import java.util.*

object GPUImageFilterUtils {
    fun getGPUFilters(context: Context) = filters.filters.mapIndexed { index, filterType ->
        filters.names[index] to createFilterForType(context, filterType)
    }

    private val filters: FilterList = FilterList().apply {
        addFilter("Contrast", FilterType.CONTRAST)
        addFilter("Invert", FilterType.INVERT)
        addFilter("Pixelation", FilterType.PIXELATION)
        addFilter("Hue", FilterType.HUE)
        addFilter("Gamma", FilterType.GAMMA)
        addFilter("Brightness", FilterType.BRIGHTNESS)
        addFilter("Sepia", FilterType.SEPIA)
        addFilter("Grayscale", FilterType.GRAYSCALE)
        addFilter("Sharpness", FilterType.SHARPEN)
        addFilter("Sobel Edge Detection", FilterType.SOBEL_EDGE_DETECTION)
        addFilter("Threshold Edge Detection", FilterType.THRESHOLD_EDGE_DETECTION)
        addFilter("3x3 Convolution", FilterType.THREE_X_THREE_CONVOLUTION)
        addFilter("Emboss", FilterType.EMBOSS)
        addFilter("Posterize", FilterType.POSTERIZE)
        addFilter("Grouped filters", FilterType.FILTER_GROUP)
        addFilter("Saturation", FilterType.SATURATION)
        addFilter("Exposure", FilterType.EXPOSURE)
        addFilter("Highlight Shadow", FilterType.HIGHLIGHT_SHADOW)
        addFilter("Monochrome", FilterType.MONOCHROME)
        addFilter("Opacity", FilterType.OPACITY)
        addFilter("RGB", FilterType.RGB)
        addFilter("White Balance", FilterType.WHITE_BALANCE)
        addFilter("Vignette", FilterType.VIGNETTE)
        addFilter("ToneCurve", FilterType.TONE_CURVE)

        addFilter("Luminance", FilterType.LUMINANCE)
        addFilter("Luminance Threshold", FilterType.LUMINANCE_THRESHSOLD)

        addFilter("Blend (Difference)", FilterType.BLEND_DIFFERENCE)
        addFilter("Blend (Source Over)", FilterType.BLEND_SOURCE_OVER)
        addFilter("Blend (Color Burn)", FilterType.BLEND_COLOR_BURN)
        addFilter("Blend (Color Dodge)", FilterType.BLEND_COLOR_DODGE)
        addFilter("Blend (Darken)", FilterType.BLEND_DARKEN)
        addFilter("Blend (Dissolve)", FilterType.BLEND_DISSOLVE)
        addFilter("Blend (Exclusion)", FilterType.BLEND_EXCLUSION)
        addFilter("Blend (Hard Light)", FilterType.BLEND_HARD_LIGHT)
        addFilter("Blend (Lighten)", FilterType.BLEND_LIGHTEN)
        addFilter("Blend (Add)", FilterType.BLEND_ADD)
        addFilter("Blend (Divide)", FilterType.BLEND_DIVIDE)
        addFilter("Blend (Multiply)", FilterType.BLEND_MULTIPLY)
        addFilter("Blend (Overlay)", FilterType.BLEND_OVERLAY)
        addFilter("Blend (Screen)", FilterType.BLEND_SCREEN)
        addFilter("Blend (Alpha)", FilterType.BLEND_ALPHA)
        addFilter("Blend (Color)", FilterType.BLEND_COLOR)
        addFilter("Blend (Hue)", FilterType.BLEND_HUE)
        addFilter("Blend (Saturation)", FilterType.BLEND_SATURATION)
        addFilter("Blend (Luminosity)", FilterType.BLEND_LUMINOSITY)
        addFilter("Blend (Linear Burn)", FilterType.BLEND_LINEAR_BURN)
        addFilter("Blend (Soft Light)", FilterType.BLEND_SOFT_LIGHT)
        addFilter("Blend (Subtract)", FilterType.BLEND_SUBTRACT)
        addFilter("Blend (Chroma Key)", FilterType.BLEND_CHROMA_KEY)
        addFilter("Blend (Normal)", FilterType.BLEND_NORMAL)

        addFilter("Lookup (Amatorka)", FilterType.LOOKUP_AMATORKA)
        addFilter("Gaussian Blur", FilterType.GAUSSIAN_BLUR)
        addFilter("Crosshatch", FilterType.CROSSHATCH)

        addFilter("Box Blur", FilterType.BOX_BLUR)
        addFilter("CGA Color Space", FilterType.CGA_COLORSPACE)
        addFilter("Dilation", FilterType.DILATION)
        addFilter("Kuwahara", FilterType.KUWAHARA)
        addFilter("RGB Dilation", FilterType.RGB_DILATION)
        addFilter("Sketch", FilterType.SKETCH)
        addFilter("Toon", FilterType.TOON)
        addFilter("Smooth Toon", FilterType.SMOOTH_TOON)
        addFilter("Halftone", FilterType.HALFTONE)

        addFilter("Bulge Distortion", FilterType.BULGE_DISTORTION)
        addFilter("Glass Sphere", FilterType.GLASS_SPHERE)
        addFilter("Haze", FilterType.HAZE)
        addFilter("Laplacian", FilterType.LAPLACIAN)
        addFilter("Non Maximum Suppression", FilterType.NON_MAXIMUM_SUPPRESSION)
        addFilter("Sphere Refraction", FilterType.SPHERE_REFRACTION)
        addFilter("Swirl", FilterType.SWIRL)
        addFilter("Weak Pixel Inclusion", FilterType.WEAK_PIXEL_INCLUSION)
        addFilter("False Color", FilterType.FALSE_COLOR)

        addFilter("Color Balance", FilterType.COLOR_BALANCE)

        addFilter("Levels Min (Mid Adjust)", FilterType.LEVELS_FILTER_MIN)

        addFilter("Bilateral Blur", FilterType.BILATERAL_BLUR)

        addFilter("Zoom Blur", FilterType.ZOOM_BLUR)

        addFilter("Transform (2-D)", FilterType.TRANSFORM2D)

        addFilter("Solarize", FilterType.SOLARIZE)

        addFilter("Vibrance", FilterType.VIBRANCE)
    }

    private fun createFilterForType(context: Context, type: FilterType): GPUImageFilter {
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
            FilterType.VIBRANCE -> GPUImageVibranceFilter()
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
        CONTRAST, GRAYSCALE, SHARPEN, SEPIA, SOBEL_EDGE_DETECTION, THRESHOLD_EDGE_DETECTION, THREE_X_THREE_CONVOLUTION, FILTER_GROUP, EMBOSS, POSTERIZE, GAMMA, BRIGHTNESS, INVERT, HUE, PIXELATION,
        SATURATION, EXPOSURE, HIGHLIGHT_SHADOW, MONOCHROME, OPACITY, RGB, WHITE_BALANCE, VIGNETTE, TONE_CURVE, LUMINANCE, LUMINANCE_THRESHSOLD, BLEND_COLOR_BURN, BLEND_COLOR_DODGE, BLEND_DARKEN,
        BLEND_DIFFERENCE, BLEND_DISSOLVE, BLEND_EXCLUSION, BLEND_SOURCE_OVER, BLEND_HARD_LIGHT, BLEND_LIGHTEN, BLEND_ADD, BLEND_DIVIDE, BLEND_MULTIPLY, BLEND_OVERLAY, BLEND_SCREEN, BLEND_ALPHA,
        BLEND_COLOR, BLEND_HUE, BLEND_SATURATION, BLEND_LUMINOSITY, BLEND_LINEAR_BURN, BLEND_SOFT_LIGHT, BLEND_SUBTRACT, BLEND_CHROMA_KEY, BLEND_NORMAL, LOOKUP_AMATORKA,
        GAUSSIAN_BLUR, CROSSHATCH, BOX_BLUR, CGA_COLORSPACE, DILATION, KUWAHARA, RGB_DILATION, SKETCH, TOON, SMOOTH_TOON, BULGE_DISTORTION, GLASS_SPHERE, HAZE, LAPLACIAN, NON_MAXIMUM_SUPPRESSION,
        SPHERE_REFRACTION, SWIRL, WEAK_PIXEL_INCLUSION, FALSE_COLOR, COLOR_BALANCE, LEVELS_FILTER_MIN, BILATERAL_BLUR, ZOOM_BLUR, HALFTONE, TRANSFORM2D, SOLARIZE, VIBRANCE
    }

    class FilterList {
        val names: MutableList<String> = LinkedList()
        val filters: MutableList<FilterType> = LinkedList()

        fun addFilter(name: String, filter: FilterType) {
            names.add(name)
            filters.add(filter)
        }
    }
}
