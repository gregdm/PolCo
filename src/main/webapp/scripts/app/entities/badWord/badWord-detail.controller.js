'use strict';

angular.module('polcoApp')
    .controller('BadWordDetailController', function ($scope, $stateParams, BadWord, GoodWord) {
        $scope.badWord = {};
        $scope.load = function (id) {
            BadWord.get({id: id}, function(result) {
              $scope.badWord = result;
            });
        };
        $scope.load($stateParams.id);
    });
