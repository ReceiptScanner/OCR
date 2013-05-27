function [ net ] = createNet( layers )
%CREATENET Summary of this function goes here
%   Detailed explanation goes here
%   layers: first value is input size, then number of neurons per layer
%   without bias/ Number of outputs is equal to the numebr of neurons
%   in the last layer

net.layers = layers;
net.weights = cell(size(layers)-1);
for i =2 : length(layers)
    net.weights(i-1) = {randn(layers(i),layers(i-1)+1)}; 
end

end

