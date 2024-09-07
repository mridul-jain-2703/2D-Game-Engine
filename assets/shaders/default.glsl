#type vertex
#version 460 core

//in -> input
//out -> output
//location = 0 -> its the VAO number i.e. attribute's location in the VBO
layout (location = 0) in vec3 aPos;
layout (location = 1) in vec4 aColor;

out vec4 fColor;

void main(){
    fColor = aColor;
    //gl_position is globally and implicitly defined variable that we is always already declared in every file.
    //here, aPos provides first 3 values and 4th value will be 1.
    gl_Position = vec4(aPos, 1.0);
}

#type fragment
#version 460 core

in vec4 fColor;
out vec4 color;

//Passing the color from the vertex shader to the fragment shader
void main(){
    color = fColor;

}
