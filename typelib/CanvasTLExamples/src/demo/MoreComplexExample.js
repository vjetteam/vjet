 
function drawCheckbox(context, element, x, y) {//< void fn(org.w3c.CanvasRenderingContext2D,HTMLElement, int,int) 
   context.save();
   context.font = '10px sans-serif';
   context.textAlign = 'left';
   
   context.textBaseline = 'middle';
   var metrics = context.measureText(element.labels[0].textContent);
   context.beginPath();
   context.strokeStyle = 'black';
   context.rect(x-5, y-5, 10, 10);
   context.stroke();
   if (element.checked) {
     context.fillStyle = 'black';
     context.fill();
   }
   context.fillText(element.labels[0].textContent, x+5, y);
   context.beginPath();
   context.rect(x-7, y-7, 12 + metrics.width+2, 14);
   if (context.drawFocusRing(element, x, y, true)) {
     context.strokeStyle = 'silver';
     context.stroke();
   }
   context.restore();
 }
 function drawBase() { /* ... */ }
 function drawAs() { /* ... */ }
 function drawBs() { /* ... */ }
 function redraw() {
   var canvas = document.getElementsByTagName('canvas')[0];
   var context = canvas.getContext('2d');
   context.clearRect(0, 0, canvas.width, canvas.height);
   drawCheckbox(context, document.getElementById('showA'), 20, 40);
   drawCheckbox(context, document.getElementById('showB'), 20, 60);
   drawBase();
   if (document.getElementById('showA').checked)
     drawAs();
   if (document.getElementById('showB').checked)
     drawBs();
 }
 function processClick(event) {
   var canvas = document.getElementsByTagName('canvas')[0];
   var context = canvas.getContext('2d');
   var x = event.clientX - canvas.offsetLeft;
   var y = event.clientY - canvas.offsetTop;
   drawCheckbox(context, document.getElementById('showA'), 20, 40);
   if (context.isPointInPath(x, y))
     document.getElementById('showA').checked = !(document.getElementById('showA').checked);
   drawCheckbox(context, document.getElementById('showB'), 20, 60);
   if (context.isPointInPath(x, y))
     document.getElementById('showB').checked = !(document.getElementById('showB').checked);
   redraw();
 }
 document.getElementsByTagName('canvas')[0].addEventListener('focus', redraw, true);
 document.getElementsByTagName('canvas')[0].addEventListener('blur', redraw, true);
 document.getElementsByTagName('canvas')[0].addEventListener('change', redraw, true);
 document.getElementsByTagName('canvas')[0].addEventListener('click', processClick, false);
 redraw();
