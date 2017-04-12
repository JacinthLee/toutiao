/**
 * Created by Shichengyao on 4/3/17.
 */
$(function() {
    var msgId;
    $(".del-link").on("click",function(e) {
        $(".mask-delete").show();
        $target= $(e.target);
        msgId = $target.data("id");

    });
    $(".mask-delete .modal-header .close,.btn.btn-default").on("click",function () {
        $(".mask-delete").hide();
    });
    $(".mask-delete").on("click",".btn.btn-danger.btn-del",function (event) {
        var target =event.currentTarget;
        $.post("/msg/delete",
            {
                msgId:msgId
            },
        function(data) {
            window.location.reload();
        });
    });
});