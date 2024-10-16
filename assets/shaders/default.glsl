#type vertex
#version 460 core

//in -> input
//out -> output
//location = 0 -> its the VAO number i.e. attribute's location in the VBO
layout (location = 0) in vec3 aPos;
layout (location = 1) in vec4 aColor;

uniform mat4 uProjection;
uniform mat4 uView;


out vec4 fColor;

void main(){
    fColor = aColor;
    //gl_position is globally and implicitly defined variable that we is always already declared in every file.
    //here, aPos provides first 3 values and 4th value will be 1.
    gl_Position = uProjection * uView * vec4(aPos, 1.0);
    //aPos gives us world cords.
    //Multiplying it with proj. and view gives us NDC i.e. Normalized Device Coordinates.
}

#type fragment
#version 460 core

uniform float uTime;

in vec4 fColor;
out vec4 color;

//Passing the color from the vertex shader to the fragment shader
void main(){
    //The average will give convert fColor to greyscale.
    //float avg = (fColor.r + fColor.g + fColor.b)/3;
    float noise = fract(sin(dot(fColor.xy ,vec2(12.9898,78.233))) * 43758.5453);
    //color = vec4(avg,avg,avg, 1);
    color = fColor * noise;
}
