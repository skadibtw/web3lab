canvas.addEventListener('click', function (event) {

        const rValue = getR();
        const rect = canvas.getBoundingClientRect();
        const xClick = event.clientX - rect.left;
        const yClick = event.clientY - rect.top;

        const canvasCenterX = canvas.width / 2;
        const canvasCenterY = canvas.height / 2;

        const scale = canvasCenterX / 5;

        const xValue = (xClick - canvasCenterX) / scale;
        const yValue = -(yClick - canvasCenterY) / scale;
        document.getElementById("hidden-form:graph-x").value = xValue;
        document.getElementById("hidden-form:graph-y").value = yValue;
        document.getElementById("hidden-form:graph-r").value = rValue;
        document.getElementById("hidden-form:graph-send").click();
    });

function getR() {
    const r = PF("widget_main_form_superSlider").getValue();
    return r;
}


function drawDot(xValue, yValue, status) {
    const canvas = document.getElementById('pointsCanvas');
    const ctx = canvas.getContext('2d');

    const plotX = xValue * 40;
    const plotY = -yValue * 40;

    ctx.beginPath();

    ctx.translate(canvas.width / 2, canvas.height / 2);
    ctx.arc(plotX, plotY, 5, 0, 2 * Math.PI);
    ctx.fillStyle = status ? 'green' : 'red';
    ctx.fill();
    ctx.resetTransform();
    ctx.closePath();
}