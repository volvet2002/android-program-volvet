package com.volvet.framework;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Vertices {
	final GLGraphics  glGraphics;
	final boolean     hasColor;
	final boolean     hasTexCoords;
	final int         vertexSize;
	final IntBuffer   vertices;
    final ShortBuffer indices;	
    final int[]       tmpBuffer;
    
    public Vertices(GLGraphics glGraphics, int maxVertices, int maxIndices, boolean hasColor, boolean hasTexCoords) {
    	this.glGraphics = glGraphics;
    	this.hasColor = hasColor;
    	this.hasTexCoords = hasTexCoords;
    	this.vertexSize = (2 + (hasColor?4:0) + (hasTexCoords?2:0))*4;
    	this.tmpBuffer = new int[maxVertices * vertexSize/4];
    	
    	ByteBuffer buffer = ByteBuffer.allocateDirect(vertexSize * maxVertices);
    	buffer.order(ByteOrder.nativeOrder());
    	vertices = buffer.asIntBuffer();
    	
    	if( maxIndices > 0 ){
    		buffer = ByteBuffer.allocateDirect(maxIndices * Short.SIZE/8);
    		buffer.order(ByteOrder.nativeOrder());
    		indices = buffer.asShortBuffer();
    	} else {
    		indices = null;
    	}    	
    }
    
    public  void setVertices(float[] vertices, int offset, int length) {
    	int len = offset + length;
    	this.vertices.clear();
    	for( int i=offset; i<len;i++ ){
    		tmpBuffer[i] = Float.floatToRawIntBits(vertices[i]);
    	}
    	this.vertices.put(tmpBuffer, offset, length);
    	this.vertices.flip();
    }
    
    public  void setIndices(short[] indices, int offset, int length){
    	this.indices.clear();    	
    	this.indices.put(indices, offset, length);
    	this.indices.flip();
    }
    
    public  void bind() {
        GL10  gl = glGraphics.getGL();
    	
    	gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
    	vertices.position(0);
    	gl.glVertexPointer(2, GL10.GL_FLOAT, vertexSize, vertices);
    	
    	if( hasColor ){
    		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
    		vertices.position(2);
    		gl.glColorPointer(4, GL10.GL_FLOAT, vertexSize, vertices);
    	}
    	
    	if( hasTexCoords ){
    		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
    		vertices.position(hasColor?6:2);
    		gl.glTexCoordPointer(2, GL10.GL_FLOAT, vertexSize, vertices);
    	}
    }
    
    public  void unbind() {
    	GL10  gl = glGraphics.getGL();
    	
    	if( hasTexCoords ){
    		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
    	}
    	
    	if( hasColor ){
    		gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
    	}
    }
    
    public  void draw(int primitiveType, int offset, int numVertices) {
    	GL10  gl = glGraphics.getGL();  	
    	    	
    	if( indices != null ){
    		indices.position(offset);
    		gl.glDrawElements(primitiveType, numVertices, GL10.GL_UNSIGNED_SHORT, indices);    		
    	} else {
    		gl.glDrawArrays(primitiveType, offset, numVertices);
    	}    	
    }
}
