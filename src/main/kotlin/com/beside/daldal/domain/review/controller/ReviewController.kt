package com.beside.daldal.domain.review.controller

import com.beside.daldal.domain.review.dto.*
import com.beside.daldal.domain.review.service.ReviewService
import com.beside.daldal.shared.exception.dto.ErrorCode
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.security.Principal

@RestController
@RequestMapping("/api/v1/review")
class ReviewController(
    private val reviewService: ReviewService
) {


    @Operation(
        operationId = "findMyReview",
        summary = "내 리뷰 조회",
        description = "내가 쓴 리뷰를 전체 조회합니다.",
        tags = ["review"]
    )
    @ApiResponses(
        value =
        [
            ApiResponse(
                responseCode = "200",
                description = "success",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ReviewDTO::class)
                )]
            ),
            ApiResponse(
                responseCode = "404",
                description = "course, member not found exception",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorCode::class)
                )]
            ),
        ]
    )
    @GetMapping("")
    fun findMyReview(principal: Principal): ResponseEntity<List<ReviewReadDTO>> {
        return ResponseEntity.ok(reviewService.findMyReview(principal.name))
    }


    @Operation(
        operationId = "findReviewById",
        summary = "id를 이용한 조회",
        description = "id를 이용해서 리뷰를 조회할 수 있습니다.",
        tags = ["review"]
    )
    @ApiResponses(
        value =
        [
            ApiResponse(
                responseCode = "200",
                description = "success",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ReviewReadDTO::class)
                )]
            ),
            ApiResponse(
                responseCode = "404",
                description = "course, member not found exception",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorCode::class)
                )]
            ),
        ]
    )
    @GetMapping("/{reviewId}")
    fun findReviewById(@PathVariable reviewId: String, principal: Principal): ResponseEntity<ReviewReadDTO> {
        return ResponseEntity.ok(reviewService.findById(principal.name, reviewId))
    }


    @Operation(
        operationId = "createReview",
        summary = "리뷰 생성",
        description = "리뷰를 생성합니다. 해당 api는 form-data 형식으로 이미지를 넘겨주고 dto는 application/json으로 넘겨야 동작합니다.",
        tags = ["review"]
    )
    @ApiResponses(
        value =
        [
            ApiResponse(
                responseCode = "200",
                description = "success",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ReviewDTO::class)
                )]
            ),
            ApiResponse(
                responseCode = "404",
                description = "course, member not found exception",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorCode::class)
                )]
            ),
        ]
    )
    @PostMapping("/{courseId}", consumes = ["multipart/form-data"])
    fun createReview(
        principal: Principal,
        @PathVariable courseId: String,
        @RequestPart("dto") dto: ReviewCreateDTO,
        @RequestPart("file") file: MultipartFile
    ): ResponseEntity<ReviewDTO> {
        return ResponseEntity.ok(reviewService.createReview(principal.name, courseId, dto, file))
    }


    @Operation(
        operationId = "deleteReview",
        summary = "리뷰 삭제",
        description = "리뷰를 삭제합니다. 리뷰의 이미지도 같이 삭제됩니다.",
        tags = ["review"]
    )
    @ApiResponses(
        value =
        [
            ApiResponse(
                responseCode = "200",
                description = "success",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = String::class)
                )]
            ),
        ]
    )
    @DeleteMapping("/{reviewId}")
    fun deleteReview(@PathVariable reviewId: String): ResponseEntity<String> {
        reviewService.deleteReview(reviewId)
        return ResponseEntity.ok(reviewId)
    }


    @Operation(
        operationId = "updateReview",
        summary = "리뷰 수정",
        description = "리뷰를 수정합니다. 해당 api는 form-data 형식으로 이미지를 넘겨주고 dto는 application/json으로 넘겨야 동작합니다.",
        tags = ["review"]
    )
    @ApiResponses(
        value =
        [
            ApiResponse(
                responseCode = "200",
                description = "success",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ReviewDTO::class)
                )]
            ),
            ApiResponse(
                responseCode = "404",
                description = "course, member not found exception",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorCode::class)
                )]
            ),
        ]
    )
    @PutMapping("/{reviewId}")
    fun updateReview(
        principal: Principal,
        @PathVariable reviewId: String,
        @RequestPart("dto") dto: ReviewUpdateDTO,
        @RequestPart("file") file: MultipartFile
    ): ResponseEntity<ReviewDTO> {
        val email = principal.name
        return ResponseEntity.ok(reviewService.updateReview(email, reviewId, dto, file))
    }

    @GetMapping("/popular")
    fun popular(principal: Principal): ResponseEntity<List<ReviewReadDTO>> =
        ResponseEntity.ok(reviewService.findPopularReview(principal.name))

}
