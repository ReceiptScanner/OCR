function [ output ] = feedForward( net, input )
%FEEDFORWARD Summary of this function goes here
%   Detailed explanation goes here
layers = net.layers;
x = input;
for i=2:length(layers)
    x = sigmoid(cell2mat(net.weights(i-1))*[1;x]);
end
output = x;
end

