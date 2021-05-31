/*
/*
 *  jQuery StarRatingSvg v1.2.0
 *
 *  http://github.com/nashio/star-rating-svg
 *  Author: Ignacio Chavez
 *  hello@ignaciochavez.com
 *  Licensed under MIT
 */

;(function ( $, window, document, undefined ) {
    let slides = document.querySelector('.slides');
    let slide = document.querySelectorAll('.slides li'),
        currentIdx = 0,
        slideCount = slide.length,
        slideWidth = 200,
        slideMargin = 25,
        prevBtn = document.querySelector('.prev'),
        nextBtn = document.querySelector('.next');

    makeClone();

    function makeClone(){
        for(let i = 0; i<slideCount;i++) {
            let cloneSlide = slide[i].cloneNode(true);
            cloneSlide.classList.add('clone');
            slides.appendChild(cloneSlide);
        }
        for(let i = slideCount-1;i>=0;i--){
            let cloneSlide = slide[i].cloneNode(true);
            cloneSlide.classList.add('clone');
            slides.prepend(cloneSlide);
        }
        updateWidth();
        setInitialPos();
        setTimeout(function (){
            slides.classList.add('animated');
        },100)
    }

    function updateWidth(){
        let currentSlides = document.querySelectorAll('.slides li');
        let newSlideCount = currentSlides.length;

        let newwidth = (slideWidth + slideMargin) *newSlideCount - slideMargin + 'px';
        slides.style.width = newwidth;
    }

    function setInitialPos(){
        let initialTranslateValue = -(slideWidth + slideMargin) *slideCount;
        slides.style.transform = 'translateX('+initialTranslateValue+'px)';
    }

    nextBtn.addEventListener('click',function (){
        moveSlide(currentIdx + 1);
    })

    prevBtn.addEventListener('click',function (){
        moveSlide(currentIdx - 1);
    })

    function moveSlide(num){
        slides.style.left = - num *(slideWidth+slideMargin) +'px';
        currentIdx = num;
        if(currentIdx === slideCount || currentIdx === -slideCount){
            setTimeout(function (){
                slides.classList.remove('animated');
                slides.style.left = '0px';
                currentIdx = 0;
            },500)
            setTimeout(function (){
                slides.classList.add('animated');
            },600);
        }
    }


})( jQuery, window, document );


