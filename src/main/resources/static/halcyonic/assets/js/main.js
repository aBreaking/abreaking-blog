/*
	Halcyonic by HTML5 UP
	html5up.net | @ajlkn
	Free for personal and commercial use under the CCA 3.0 license (html5up.net/license)
*/

(function($) {

	var $window = $(window),
		$body = $('body'),
        $content = $("#content");

	// Breakpoints.
		breakpoints({
			xlarge:  [ '1281px',  '1680px' ],
			large:   [ '981px',   '1280px' ],
			medium:  [ '737px',   '980px'  ],
			small:   [ null,      '736px'  ]
		});
    // Hack: Enable IE workarounds.
    if (browser.name == 'ie')
        $body.addClass('ie');

    // Touch?
    if (browser.mobile)
        $body.addClass('touch');

    // Transitions supported?
    if (browser.canUse('transition')) {

        var a = window.setTimeout(function () {
            $("#isPreload").text("后台正在拼命处理中....");
        },6000);
        var b = window.setTimeout(function () {
            $("#isPreload").text("可能第一次打开本站有点慢....");
        },9000);
        var c = window.setTimeout(function () {
            $("#isPreload").text("还是没打开？这该死的服务器!!!");
        },12000);
        var d = window.setTimeout(function () {
            $("#isPreload").text("要不重新刷新下本站呢");
        },16000);
        // Play initial animations on page load.
        $window.on('load', function() {
            window.setTimeout(function() {
                $body.removeClass('is-preload');
                $("#isPreload").hide();
                window.clearTimeout(a);
                window.clearTimeout(b);
                window.clearTimeout(c);
                window.clearTimeout(d);
                return;
            }, 100);

        });

        // Prevent transitions/animations on resize.
        var resizeTimeout;

        $window.on('resize', function() {

            window.clearTimeout(resizeTimeout);

            $body.addClass('is-resizing');

            resizeTimeout = window.setTimeout(function() {
                $body.removeClass('is-resizing');
            }, 100);

        });
    }

		// Title Bar.
			$(
				'<div id="titleBar">' +
					'<a href="#navPanel" class="toggle"></a>' +
					'<a href="'+$("#nav .active").attr("href")+'" class="title">'+$("#nav .active").text()+'</a>' +
				'</div>'
			)
				.appendTo($body);

		// Panel.
			$(
				'<div id="navPanel">' +
					'<div id="abreaking">'+
                	$('#abreaking').html()+
					'</div>'+
					'<nav>' +
						$('#nav').navList() +
					'</nav>' +
				'</div>'
			)
				.appendTo($body)
				.panel({
					delay: 500,
					hideOnClick: true,
					hideOnSwipe: true,
					resetScroll: true,
					resetForms: true,
					side: 'left',
					target: $body,
					visibleClass: 'navPanel-visible'
				});

                $content.poptrox({
                    baseZIndex: 20000,
                    caption: function($a) {

                        var s = '';

                        $a.nextAll().each(function() {
                            s += this.outerHTML;
                        });

                        return s;

                    },
                    fadeSpeed: 300,
                    onPopupClose: function() { $body.removeClass('modal-active'); },
                    onPopupOpen: function() { $body.addClass('modal-active'); },
                    overlayOpacity: 0,
                    popupCloserText: '',
                    popupHeight: 150,
                    popupLoaderText: '',
                    popupSpeed: 300,
                    popupWidth: 150,
                    selector: '.thumb > a',
                    usePopupCaption: false,
                    usePopupCloser: true,
                    usePopupDefaultStyling: false,
                    usePopupForceClose: true,
                    usePopupLoader: true,
                    usePopupNav: false,
                    windowMargin: 50
                });

                // Hack: Set margins to 0 when 'xsmall' activates.
                breakpoints.on('<=xsmall', function() {
                    $main[0]._poptrox.windowMargin = 0;
                });

                breakpoints.on('>xsmall', function() {
                    $main[0]._poptrox.windowMargin = 50;
                });

})(jQuery);

/**
 * 滚动到顶部
 * @param speed 速度，1-10
 */
function scroll2Top(speed){
    var d = speed?speed:5;
    var $document = $(document);
    var intervalId = setInterval(function() {
        if ($(document).scrollTop() <= 0){
            clearInterval(intervalId);
        }
        $document.scrollTop($document.scrollTop() + (-20 * d));
        d+=0.5;
    }, 15);
}

/**
 * 提交评论
 * @returns {boolean}
 */
function subComment() {
    $.ajax({
        type: 'post',
        url: '/comment',
        data: $('#comment-form').serialize(),
        /*async: false,*/
        dataType: 'json',
        success: function (result) {
            $('#comment-form input[name=coid]').val('');
            if (result && result.success) {
                alert("评论已提交至后台审核!");
                $("#comment-form")[0].reset();
                return true;
            } else {
                if (result.msg) {
                    alert(result.msg);
                }
            }
        }
    });
    return false;
}

/**
 * 去别的网站
 * @param href
 */
function goHref(href){
	window.open(href);
}