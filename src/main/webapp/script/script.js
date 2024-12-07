    document.getElementById('click_catcher').addEventListener('mousemove', evt => {
    let target = evt.target;

    let offset = getCoords(evt.target);
    const width = offset.right - offset.left;
    const height = offset.bottom - offset.top;

    evt.target = target;
        let x = ((evt.pageX - offset.left - 150) / 120) * (5 / 2);
        let y = -((evt.pageY - offset.top - 150) / 120) * (5 / 2);


    drawOMarker(x, y);

    document.getElementById("hidden-form:graph-x").value = x;
    document.getElementById("hidden-form:graph-y").value = y;
    document.getElementById("hidden-form:graph-r").value = getR();
});

document.getElementById('click_catcher').addEventListener('mouseleave', deleteOMarker);

document.getElementById('click_catcher').addEventListener('click', () => {
    document.getElementById("hidden-form:graph-send").click();
});

function handleSlide(event) {
    redrawFigure(getR());
}

function getR() {
    const r = PF("widget_main_form_superSlider").getValue();
    return r;
}

function getCoords(elem) {
    let box = elem.getBoundingClientRect();

    return {
        top: box.top + window.scrollY,
        right: box.right + window.scrollX,
        bottom: box.bottom + window.scrollY,
        left: box.left + window.scrollY
    };
}

function drawOMarker(x, y) {
    deleteOMarker();
    const svg = document.getElementById('graph');

    let circle = document.createElementNS("http://www.w3.org/2000/svg", 'circle');
    circle.setAttributeNS(null, 'cx', ((x) * 23 + 160).toString());
    circle.setAttributeNS(null, 'cy', (-y * 23 + 160).toString());
    circle.setAttributeNS(null, 'r', '8');
    circle.setAttributeNS(null, 'stroke', 'rgb(174, 193, 187)');
    circle.setAttributeNS(null, 'stroke-width', '5');
    circle.setAttributeNS(null, 'fill-opacity', '0');
    circle.id = "selected_pos";
    svg.appendChild(circle);
}

function deleteOMarker() {
    let circle = document.getElementById('selected_pos');
    if (circle !== null) circle.parentElement.removeChild(circle);
}

function drawDot(x, y, checked) {
    const svg = document.getElementById('graph');

    let circle = document.createElementNS("http://www.w3.org/2000/svg", 'circle');
    circle.setAttributeNS(null, 'cx', ((x) * 23 + 160).toString());
    circle.setAttributeNS(null, 'cy', (-y * 23 + 160).toString());
    circle.setAttributeNS(null, 'r', '4');
    circle.classList.add("littleDot");

    if (checked) {
        circle.setAttributeNS(null, 'fill', 'mediumspringgreen');
    } else {
        circle.setAttributeNS(null, 'fill', 'palevioletred');
    }
    svg.appendChild(circle);
}

function redrawFigure(scale) {
    const radius = 20 * scale;

    // Четверть круга
    let quarterCircle = document.getElementById("quarterCircle");
    quarterCircle.setAttributeNS(
        null,
        "d",
        `M 150 150 L 150 ${180 + radius} A ${radius + 80} ${radius + 80} 0 0 1 ${125 - radius} 150 Z`
    );

    // Прямоугольник
    let rectangle = document.getElementById("rectangle");
    rectangle.setAttributeNS(
        null,
        "points",
        `150,150 150,${230 + radius} ${180 + radius},${230 + radius} ${180 + radius},150`
    );

    // Треугольник
    let triangle = document.getElementById("triangle");
    triangle.setAttributeNS(
        null,
        "points",
        `${70 - radius},150 150,150 150,${120 - radius}`
    );

}